package com.aiops.uim.mcs.models;

import com.ca.uim.ce.tnt2.TNT2Controller;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.nimsoft.nimbus.NimException;
import com.nimsoft.nimbus.NimLog;
import com.nimsoft.pf.common.exception.ExceptionHandler;
import com.nimsoft.selfservice.exceptions.DeployerException;
import com.nimsoft.selfservice.exceptions.InvalidAddressException;
import com.nimsoft.selfservice.exceptions.ProbeNotInstalledException;
import com.nimsoft.selfservice.exceptions.RobotUnreachableException;
import com.nimsoft.selfservice.util.BusUtil;
import com.nimsoft.selfservice.v2.controller.MessageController;
import com.nimsoft.selfservice.v2.database.DataSourceManager;
import com.nimsoft.selfservice.v2.model.Callback;
import com.nimsoft.selfservice.v2.model.ConfigValue;
import com.nimsoft.selfservice.v2.model.Constants;
import com.nimsoft.selfservice.v2.model.Context;
import com.nimsoft.selfservice.v2.model.InvalidFieldException;
//import com.nimsoft.selfservice.v2.model.SelectableObject;
//import com.nimsoft.selfservice.v2.model.bean.FieldBean;
import com.nimsoft.selfservice.v2.utility.SSRUtil;
import com.nimsoft.selfservice.v2.utility.SimpleLogger;
import com.nimsoft.selfservice.v2.utility.WaspUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * In V2, templates have components. Components can be Containers (panels),
 * Fieldgroups, or Fields of different types (text field, checkbox, etc)
 *
 * Field attributes: label, read-only, type, object value, validation, required
 * Value attributes: label, value object Field types:
 *
 * "text" (length, default to stretch) "textarea" (number of lines, default to
 * lines in text) "checkbox" "objectselect" Similar to combobox, but the
 * selection is from a list of objects. The entire selectable object should be
 * returned. The "shortName" attribute of the objects should be shown in the
 * list. "combobox" (options, editable or not, defaults to not editable)
 * "datepicker" "timezone" "dynamic" This one should show the "longName"
 * attribute of the currently selected object in the "objectselect" combobox
 * referred to in the "value" attribute. Readonly.
 *
 * Text (length, default to stretch) TextArea (number of lines, default to lines
 * in text) CheckBox ComboBox (options, editable or not, defaults to not
 * editable) DatePicker TimeZone ComboBox
 *
 *
 */
@XmlRootElement(name = "field")
@XmlAccessorType(XmlAccessType.NONE)
public class Field extends FieldBean {

	private static final SimpleLogger logger = SimpleLogger.getLogger(Field.class);

	private static final int TIME_BETWEEN_RETRIES = 500;
	private static final int RETRIES_AFTER_PROBE_INSTALLED = 40;
	
	@JsonIgnore
	private String attributes; // Any attributes we may want to pass to the
	// value of the relatedField is
	// one of the values in here
	@XmlElement(name = "relatedFieldName")
	private String relatedFieldName = null; // This one is used when saving
	
	@JsonIgnore
	private Integer configValueId; // The id of the ConfigValue - this makes the
	// private Field condition; // If set, this field only applies if that
	// condition.value is not set to false, null, no or 0.
	@XmlElement(name = "callbackObject")
	private Callback callbackObject;
	
	@JsonIgnore
	private ArrayList<String> usedEntries;

	@JsonIgnore
	@XmlTransient
	private boolean filtered = false;
	
	@JsonIgnore
	@XmlTransient
	private ArrayList<String> backupOptions;
	
	@JsonIgnore
	@XmlTransient
	private ArrayList<SelectableObject> backupValues;

	@JsonIgnore
	@XmlTransient
	private boolean hasFilterFields = false;

	public boolean hasFilterFields() {
		return hasFilterFields;
	}

	public void setHasFilterFields(boolean hasFilterFields) {
		this.hasFilterFields = hasFilterFields;
	}

	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}

	public Field() {
		
		if (type==null)
			type="text";
	}

	public Field(String type, String name, String label, String cfgkey, String validation, String datatype, String helptext,
			int length, boolean required, Object value, boolean readonly, ArrayList<SelectableObject> values) {
		this.type = type;
		this.name = name;
		this.label = label;
		this.cfgkey = cfgkey;
		this.validation = validation;
		this.datatype = datatype;
		this.helptext = helptext;
		this.length = length;
		this.required = required;
		setValue(value);
		this.readonly = readonly;
		this.values = values;
		defaultcfgkey = cfgkey;
		
		if (type==null)
				type="text";

	}

	public Field(ConfigValue cv) {
		id = cv.getField();
		defaultcfgkey = cv.getDefaultKey();
		defaultValue = cv.getDefaultValue();
		cfgkey = cv.getKey();
		value = cv.getValue();
		variable = cv.getVariable();
	}

	public void addOption(String s) {
		if (options == null) {
			options = new ArrayList<String>();
		}
		options.add(s);
	}

	public void addValue(SelectableObject o) {
		if (values == null) {
			values = new ArrayList<SelectableObject>();
		}
		if (o != null && !values.contains(o)) {
			values.add(o);
		}
	}

	/**
	 * When after substitution we have a "true" or "false" string in the
	 * cv.value, we want to apply the validation to translate it into yes|no
	 * etc.
	 *
	 * @param cv
	 */
	public void fixSubstitutedCV(ConfigValue cv) {
		if (datatype != null && datatype.equals(Constants.BOOLEAN) && validation != null) { // Sometimes (after
			// substitution) we
			// end up with a literal string that
			// says "true" or "false"
			final String val = cv.getValue();
			final String[] vals = validation.split("\\|");
			if (vals.length != 2) {
				cv.setValue(getValue().toString());
			} else {
				if (val.equalsIgnoreCase("true")) {
					cv.setValue(vals[0]);
				} else if (val.equalsIgnoreCase("false")) {
					cv.setValue(vals[1]);
				}
			}
		}
	}

	private String trim(String value) {
		return value != null ? value.trim() : value;
	}

	public String getAttributes() {
		return attributes;
	}

	public Callback getCallbackObject() {
		return callbackObject;
	}

	/*public ConfigValue getConfigValue() {
		final ConfigValue cv = new ConfigValue();
		cv.setDefaultKey(defaultcfgkey);
		cv.setDefaultValue(defaultValue); // FIXME this may need the same kind of treatment as the value
		cv.setKey(getCfgkey());
		cv.setVariable(getVariable());
		cv.setField(getId());
		if (configValueId != null && configValueId.intValue() != 0) {
			cv.setId(configValueId);
		}

		if (value == null) {
			cv.setValue(null);
			return cv;
		}

		if (Constants.OBJECTSELECT.equals(type) && getSoValue() != null) {
			final SelectableObject so = getSoValue();
			cv.setValue(so.getValue());
		} else if (Constants.COMBOBOX.equals(type) && getSoValue() != null) {
			final SelectableObject so = getSoValue();
			cv.setValue(so.getIdentifier());
		} else if (Constants.CHECKBOX.equals(getType()) && validation != null) { // Important!
			// This is intended to translate true & false into yes & no, y/n,
			// 0/1.
			// The field needs to be defined with validation as "yes|no", always
			// with the positive answer first.
			try {
				final String[] vals = validation.split("\\|");
				Boolean val = false;
				if (value.getClass() == Boolean.class) {
					val = (Boolean) value;
				} else if (value.getClass() == String.class) {
					val = ((String) value).equalsIgnoreCase("yes") || ((String) value).equalsIgnoreCase("true") || ((String) value)
							.equalsIgnoreCase(vals[0]);
				}

				if (vals.length != 2) {
					cv.setValue(getValue().toString());
				} else {
					if (val) {
						cv.setValue(vals[0]);
					} else {
						cv.setValue(vals[1]);
					}
				}

			} catch (final ClassCastException e) { // This is just in case the UI
				// already interpreted the
				// validation string.
				cv.setValue(getValue().toString());
			}
		} else if (getDatatype() != null && getDatatype().equals(Constants.BOOLEAN)) { // Sometimes
			// we
			// end
			// up
			// with
			// a
			// literal
			// string
			// that
			// says
			// "true"
			// or
			// "false"
			final String val = value.toString();
			String validationString = validation;
			if (validationString == null) {
				validationString = "true|false";
			}
			final String[] vals = validationString.split("\\|");
			if (vals.length != 2) {
				cv.setValue(getValue().toString());
			} else {
				if (val.equalsIgnoreCase("true")) {
					cv.setValue(vals[0]);
				} else if (val.equalsIgnoreCase("false")) {
					cv.setValue(vals[1]);
				}
			}
		} else if (datatype != null && datatype.equalsIgnoreCase(Constants.DATATYPE_DATE)) {
			// we need to convert the date into a parseable string here so we
			// can turn it back into a date lateron
			final DateFormat df = new SimpleDateFormat(Constants.CONFIG_VALUE_TIME_FORMAT);
			final String dateString = df.format(value);
			cv.setValue(dateString);

		} else if (datatype != null && (datatype.equals(Constants.INTEGER) || datatype.equals(Constants.DOUBLE) || datatype
				.equals(Constants.FLOAT)) && (getValue() == null || getValue().toString().isEmpty() || getValue().toString().equals(
						"NaN"))) {
			cv.setValue(null);
		} else {
			cv.setValue(getValue().toString());
		}

		return cv;
	}*/

	public Integer getConfigValueId() {
		return configValueId;
	}

	/**
	 * Gets the identifier for the selected object
	 *
	 * @return
	 */
	@JsonIgnore
	public String getIdentifier() {
		// if (type.equals(OBJECTSELECT) && value!=null) {
		// log.log(4,"This is a OBJECTSELECT box and the value is of class: "+value.getClass().getName());
		// //
		// log.log(4,"Field name: "+name); // TODO
		if (value == null) {
			return null;
		}
		if (value.getClass().equals(SelectableObject.class)) {
			final SelectableObject so = (SelectableObject) value;
			return so.getIdentifier();
		} else {
			return value.toString();
			// }
		}

	}

	public ArrayList<String> getOptions() {
		return options;
	}

	public String getRelatedFieldName() {
		return relatedFieldName;
	}

	public ArrayList<String> getUsedEntries() {
		return usedEntries;
	}

	/**
	 * Get the used variables out of a field. Is used to validate the Template !?
	 *
	 * @return anything in cfgkey or value that has {} around it
	 */
	public ArrayList<String> getUsedVariables() {
		final ArrayList<String> usedVars = new ArrayList<>();
		String string = "";
		if (getCfgkey() != null) {
			string = string + getCfgkey();
		}
		if (getValue() != null && getValue(false).getClass().equals(String.class)) {
			string = string + getValue();
		}
		if (!string.isEmpty()) {
			final Pattern pattern = Pattern.compile("\\{([\\w-:/]+)\\}");
			final Matcher m = pattern.matcher(string);

			while (m.find()) {
				final String exp = m.group();
				usedVars.add(exp);
			}
		}
		return usedVars;
	}

	public SelectableObject getSoValue() {
		if (type != null && type.equalsIgnoreCase(Constants.OBJECTSELECT) && value != null && value.getClass().equals(String.class)) {
			final SelectableObject so = SelectableObject.parse((String) value);
			if (so != null) {
				return so;
			}
		} else if (Constants.COMBOBOX.equalsIgnoreCase(type) && value != null && value.getClass().equals(String.class)) {
			final String identifier = (String) value;
			// ok so this is a combobox. That means we saved only the identifier
			final ArrayList<SelectableObject> options = getValues();
			if (options != null) {
				for (final SelectableObject option : options) {
					if (identifier.equalsIgnoreCase(option.getIdentifier())) {
						return option;
					}
				}
			}
		} else if (value != null && value.getClass().equals(SelectableObject.class)) {
			return (SelectableObject) value;
		}
		return null;
	}

	public Object getValue()  {
		return getValue(true);
	}

	// added kludge for caller to indicate whether the selected object is desired return type or the selected objects identifier
	// to be compatible with what the SSR code expects vs the USM 8.2 codebase
	public Object getValue(boolean selectedObjectIdentifier) {
		if (Constants.COMBOBOX.equals(type) && value != null && value.getClass().equals(String.class)) {
			final SelectableObject so = SelectableObject.parse((String) value);
			if (so != null) {
				return so.getIdentifier();
			}
		} else if (Constants.OBJECTSELECT.equals(type) && value != null && value.getClass().equals(String.class)) {
			final SelectableObject so = SelectableObject.parse((String) value);
			if (so != null) {
				return so.getValue();
			}
		} else if (value != null && value.getClass().equals(SelectableObject.class)) {
			// called from USM SsrUtils and expects a string value returned
			final SelectableObject so = (SelectableObject) value;
			return (selectedObjectIdentifier ? so.getIdentifier() : so);
		}

		if (calculation != null && NumberUtils.isNumber(calculation) && value != null) {
			Integer intval = null;
			if (String.class == value.getClass() && ((String) value).matches("\\d+")) {
				intval = Integer.parseInt((String) value);
			} else if (Integer.class == value.getClass()) {
				intval = (Integer) value;
			}
			if (intval != null) {
				final Double factor = Double.parseDouble(calculation);
				final Double calculatedValue = factor * intval;
				final DecimalFormat df = new DecimalFormat("#.00");
				if (Constants.STRING.equals(datatype)) {
					return df.format(calculatedValue);
				} else {
					return calculatedValue;
				}
			}
		}
		return value;
	}

	@JsonIgnore
	public String getValueAsString() {
		final Object v = getValue();
		if (v == null) {
			return null;
		}

		if (v instanceof SelectableObject) {
			if (Constants.COMBOBOX.equalsIgnoreCase(type)) {
				return ((SelectableObject) v).getIdentifier();
			}
			else {
				return ((SelectableObject) v).getValue();
			}
		}

		return v.toString();
	}

	public Integer getValueAsInt() {
		final Object v = getValue();
		if (v == null) {
			return null;
		}
		if (v instanceof Integer) {
			return (Integer) v;
		}

		String valueToParse;
		if (v instanceof SelectableObject) {
			valueToParse = ((SelectableObject) v).getValue();
		} else {
			valueToParse = v.toString();
		}

		try {
			return Integer.parseInt(valueToParse);
		} catch (final Exception e) {
			return null;
		}

	}
	
	public boolean getValueAsBoolean() {
		final Object v = getValue();
		if (v == null) {
			return false;
		}
		if (v.equals("yes"))
			return true;
		else
			return false;
		
	}

	public ArrayList<SelectableObject> getValues() {
		return values;
	}

	/**
	 * Returns a map of all variables that can be addressed in this field. Only
	 * really applicable for callback fields
	 *
	 * @return
	 */
	@JsonIgnore
	public Map<String, String> getVariables() {
		// log.log(4,"getVariables called.");
		if ((variable == null || variable.isEmpty()) && type != Constants.OBJECTSELECT) {
			return null;
		}
		final HashMap<String, String> variables = new HashMap<String, String>();
		if (value != null) {
			if (getSoValue() == null) {
				if(!SSRUtil.containsSubstitutionPattern(value.toString()) ||
						!value.equals(Constants.VARIABLE_PREFIX + variable + Constants.VARIABLE_SUFFIX)) {
					variables.put(variable, value.toString());
				}
			} else {
				if (variable == null || variable.isEmpty()) {
					variable = name;
				}
				final SelectableObject so = getSoValue();
				final HashMap<String, String> soAttributes = so.getAttributes();
				if (soAttributes != null) {
					for (final Map.Entry<String, String> attribute  : soAttributes.entrySet()) {
						variables.put(variable + Constants.VARIABLE_SEPARATOR + attribute.getKey(), attribute.getValue());
					}
				}
				// In case of combobox, use the value or the identifier.
				if (so.getValue() != null) {
					variables.put(variable, so.getValue());
				} else if (so.getIdentifier() != null) {
					variables.put(variable, so.getIdentifier());
				}
				// Maintain some legacy behavior where selectable objects have some additional
				// variables (specially /value and /displayValue) added that could be used for
				// non-hidden fields.  Adding these variables here so they can also be used for
				// hidden fields.
				final Map<String, Object> additionalVariables = so.getAsMap();
				if (additionalVariables != null) {
					for (final Map.Entry<String, Object> entry : additionalVariables.entrySet()) {
						variables.put(variable + Constants.VARIABLE_SEPARATOR + entry.getKey(), String.valueOf(entry.getValue()));
					}
				}
			}
		}
		return variables;
	}

	/**
	 * Does this field have a callback?
	 *
	 * @return
	 */
	public boolean hasCallback() {
		return callback != null && callback != 0;
	}

	/**
	 * Invoke any callback defined, but only if this field is not readonly. If
	 * it is immutable, then any entries we get back from the callback that are
	 * already used in other profiles will be removed from the selection list.
	 *
	 * @throws RobotUnreachableException
	 * @throws ProbeNotInstalledException
	 * @throws NimException
	 */
	public void invokeCallback(Context context, Integer cs_id, boolean triggeredByFieldUpdate) throws RobotUnreachableException, ProbeNotInstalledException, DeployerException {
		logger.debug("No values yet, invoking callback");
		logger.debug("invokeCallback(" + context + ", " + cs_id + ")");
		final long start = System.currentTimeMillis();
		try {
			if (callbackObject != null) { // If it is immutable and existing
				// profile, it will be set to
				// readonly already.
				if (callbackObject.getTargetRobotAddress() == null) {
					final String robotAddress = TNT2Controller.getInstance().getRobotAddressForComputerSystem(cs_id);
					try {
						callbackObject.setTargetRobotAddress(robotAddress);
					} catch (InvalidAddressException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				runCB(cs_id, triggeredByFieldUpdate);
			}
		} catch (final NimException e) {
			// Check if robot is alive
			final String targetRobotAddress = callbackObject.getTargetRobotAddress();
			if (!BusUtil.getInstance().isRobotAlive(targetRobotAddress)) {
				throw new RobotUnreachableException(
						e,
						"The robot could not be reached under its registered address (" + targetRobotAddress + "). This could indicate a networking issue or that the robot is currently down. Please retry later. If the problem persists please contact your CA UIM Administrator");
				// Check if probe is installed
			}

			final String probeName = callbackObject.getProbeName();
			final String probeVersion = callbackObject.getProbeVersion();
			if (!BusUtil.getInstance().isProbeInstalled(targetRobotAddress, probeName, probeVersion)) {
				if (Context.DEVICEGROUP == context) {
					try {
						BusUtil.getInstance().installProbe(targetRobotAddress, probeName, probeVersion);
					} catch (final DeployerException e1) {
						throw new ProbeNotInstalledException(e);
					}
				} else {
					throw new ProbeNotInstalledException(e);
				}
			}
			int i = 0;
			while (i < RETRIES_AFTER_PROBE_INSTALLED && (values == null || values.isEmpty())) {
				try {
					Thread.sleep(TIME_BETWEEN_RETRIES);
					runCB(cs_id, triggeredByFieldUpdate);
				} catch (NimException e1) {
					// Retry a couple of times until the probe is ready to
					// respond.
					// isprobeinstalled will return positive before the probe is
					// ready to respond.
					if (e1.getCode() != NimException.E_NOENT) {
						return; // We get code 4 if the probe is not found.
					}
					e1 = null;
				} catch (final InterruptedException e1) {
					ExceptionHandler.getInstance().suppressAsDebug().handle(ExceptionHandler.USE_INTERNAL_MESSAGE, e);
					return;
				}
			}
			if((values == null || values.isEmpty())){
				//TODO: was CallbackCommunicationException
				MessageController.setMessage(
						MessageFormat.format(
								"Encountered error {2} while invoking command {1} on probe {0}",
								String.format("%s/%s", targetRobotAddress, probeName),
								callbackObject.getCallbackName(),
								NimException.E_CALLBACK
								)
						);
			}
		}
		final long end = System.currentTimeMillis();
		logger.debugLow("Callback invocation took " + (end - start) + "ms");
	}

	private void runCB(Integer cs_id, boolean triggeredByFieldUpdate) throws NimException, RobotUnreachableException {
		/*
		 * values = callbackObject.invoke(cs_id, triggeredByFieldUpdate); if (values ==
		 * null || values.isEmpty()) { return; }
		 * 
		 * // remove any selected object values which have no identifierMapping
		 * ArrayList<SelectableObject> badValues = new ArrayList<SelectableObject>();
		 * for (SelectableObject so : values) { if
		 * (StringUtils.trimToNull(so.getIdentifier()) == null) { badValues.add(so); } }
		 * values.removeAll(badValues); selectCurrentValuesSelectableObject();
		 */
	}

	public void selectCurrentValuesSelectableObject() {
		SelectableObject currentValue = findCurrentValue();
		if (values == null) {
			values = new ArrayList<>();
		}

		if (BooleanUtils.isTrue(readonly)) {
			values.add(currentValue);
		}
		if (usedEntries != null && !usedEntries.isEmpty()) {
			// remove the used entries from the list
			for (final Iterator<SelectableObject> it = values.iterator(); it.hasNext();) {

				final SelectableObject nextSo = it.next();
				if (usedEntries.contains(nextSo.getIdentifier())) {
					it.remove();
				}
			}
		}

		if (currentValue != null && values.contains(currentValue)) {
			values.remove(currentValue);
			values.add(0, currentValue);
		}
		else {
			// currentValue is not in the available list so set blank
			final SelectableObject emptyEntry = new SelectableObject("", "", "", null);
			emptyEntry.setIdentifier("");
			values.add(0, emptyEntry);
		}

		value = values.get(0);
	}

	public boolean isProfileName() {
		return name != null && name.equalsIgnoreCase(Constants.PROFILENAME) || variable != null && variable
				.equalsIgnoreCase(Constants.PROFILENAME);
	}

	/**
	 * Moves the current value to the top of the selection list, so it gets
	 * displayed correctly.
	 */
	public void moveCurrentValueToTop() {
		// Move the current value to the top of the list
		if (values != null && !values.isEmpty() && value != null) {
			SelectableObject current = null;
			if (value.getClass() != SelectableObject.class) {
				current = findCurrentValue();

				if (current != null) {
					values.remove(current);
					values.add(0, current);
				}
			}
		} else if (value != null && value.getClass() == SelectableObject.class) {
			values = new ArrayList<SelectableObject>();
			values.add((SelectableObject) value);
		}

	}

	public void save() throws InvalidFieldException {
		//final FieldDao fd = FieldDao.getInstance();
		//fd.add(this);
		// TODO save any options
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public void setCallbackObject(Callback callbackObject) {
		this.callbackObject = callbackObject;
	}

	public void setConfigValueId(Integer configValueId) {
		this.configValueId = configValueId;
	}

	/**
	 * Checks users rights to see / change this field. Needs to be called after
	 * loading.
	 *
	 */
	public void setDerivedAttributes() {
		// If it's a spacer, don't bother
		if (cfgkey == null && variable == null) {
			return;
		}
		// Check readonly
		if (immutable && configValueId != null && configValueId.intValue() != 0) {
			readonly = true;
		}
		// IF running in wasp, check acl
		if (acl != null && !acl.isEmpty() && DataSourceManager.runningInWasp && !WaspUtil.getInstance().isUserInRole(acl)) {
			// Hide the field, and don't allow updates
			readonly = true;
			hidden = true;
		}
		// Set the boolean value to true or false, instead of what was save in
		// the database.
		if (value != null && value.getClass().equals(String.class) && validation != null && datatype.equals(Constants.BOOLEAN)) {
			// This is intended to translate true & false into yes & no, y/n,
			// 0/1.
			// The field needs to be defined with validation as "yes|no", always
			// with the positive answer first.

			final String dbvalue = (String) value;
			final String[] vals = validation.split("\\|");
			if (vals.length == 2) {
				if (dbvalue.equals(vals[0])) {
					value = Boolean.TRUE;
				} else if (dbvalue.equals(vals[1])) {
					value = Boolean.FALSE;
				}
			}
		}
		if (value != null && value.getClass() == String.class) {
			final String val = (String) value;
			if (!val.isEmpty()) {
				try {
					if (Constants.INTEGER.equals(datatype)) {
						value = Integer.parseInt((String) value);
					} else if (Constants.FLOAT.equals(datatype)) {
						value = Float.parseFloat((String) value);
					} else if (Constants.DOUBLE.equals(datatype)) {
						value = Double.parseDouble((String) value);
					} else if (Constants.DATATYPE_DATE.equalsIgnoreCase(datatype)) {
						final DateFormat df = new SimpleDateFormat(Constants.CONFIG_VALUE_TIME_FORMAT);
						try {
							value = df.parse((String) value);
						} catch (ParseException e) {
							// FIXME: how should we properly handle this?
							e = null; // ignore for now
						}
					}
				} catch (NumberFormatException e) {
					// log.log(NimLog.ERROR,"Data in field is inconsistent with datatype: "+datatype+" "+val+" field id: "+id);
					e = null; // ignore
				}
			}
		}
	}

	public void setOptions(ArrayList<String> options) {
		this.options = options;
	}

	public void setRelatedFieldName(String relatedFieldName) {
		this.relatedFieldName = relatedFieldName;
	}

	public void setUsedEntries(ArrayList<String> usedEntries) {
		this.usedEntries = usedEntries;
	}

	public void setValue(Object value) {

		// TODO: (chris) I believe this would be a better way to do the
		// conversion instead of in "setDerivedAttributes"...
		// we need to convert the incoming string value back to the datatype it
		// is supposed to be in case it does not have the right datatype already
		if (value == null) {
			this.value = null;
			return;
		}

		if (value.getClass().equals(String.class) && ((String) value).equalsIgnoreCase("<auto populated by foreach>")) {
			this.value = value;
			return;
		}

		if (Constants.OBJECTSELECT.equalsIgnoreCase(type) || Constants.COMBOBOX.equalsIgnoreCase(type)) {
			// the value should contain a serialized SelectableObject,
			// deserialize it
			if(value.getClass().equals(String.class)) {
				SelectableObject so = SelectableObject.parse((String) value);
				if (so != null) {
					value = so;
				}
			}
		} else if (Constants.STRING.equalsIgnoreCase(datatype) && Constants.CHECKBOX.equalsIgnoreCase(type) && validation != null && validation
				.contains("|") && !validation.equals("yes|no")) {
			value = parseBoolean(String.valueOf(value));
		} else if (Constants.STRING.equalsIgnoreCase(datatype) && !String.class.isInstance(value)) {
			final String stringVal = String.valueOf(value);
			value = stringVal;
		} else if (Constants.BOOLEAN.equalsIgnoreCase(datatype) && !Boolean.class.isInstance(value) && value instanceof String) {
			// we need to convert this into a boolean
			if (((String) value).equalsIgnoreCase("0") || ((String) value).equalsIgnoreCase("false") || ((String) value)
					.equalsIgnoreCase("no")) {
				value = false;
			} else if (((String) value).equalsIgnoreCase("1") || ((String) value).equalsIgnoreCase("true") || ((String) value)
					.equalsIgnoreCase("yes")) {
				value = true;
			}
		} else if (Constants.INTEGER.equalsIgnoreCase(datatype) && !Integer.class.isInstance(value)) {
			// For large numbers, the UI sometimes sends the Integers as a Double
			if (value instanceof Double && com.nimsoft.selfservice.v2.utility.NumberUtils.isInteger((Double)value)) {
				value = ((Double)value).intValue();
			} else if (value instanceof String && StringUtils.isNumeric((String)value)) {
				value = Integer.valueOf((String)value);
			} else {
				// the value is supposed to be a number but is NOT an integer, valid double, or a numeric string so clear it
				value = null;
			}
		} else if (Constants.DOUBLE.equalsIgnoreCase(datatype) && !Double.class.isInstance(value) && value instanceof String && ((String) value)
				.length() > 0) {
			value = Double.parseDouble((String) value);
		} else if (Constants.DATATYPE_DATE.equalsIgnoreCase(datatype) && !Date.class.isInstance(value) && value instanceof String && ((String) value)
				.length() > 0) {
			final DateFormat df = new SimpleDateFormat(Constants.CONFIG_VALUE_TIME_FORMAT);
			try {
				value = df.parse((String) value);
			} catch (ParseException e) {
				logger.error(e.getMessage());
				// FIXME: how should we properly handle this?
				e = null; // ignore for now
			}
		}
		this.value = value;
	}

	/**
	 * Helper method to work around an issue with checkbox fields
	 *
	 * @param string
	 * @return
	 */
	private boolean parseBoolean(String string) {
		if (string == null || string.isEmpty()) {
			return false;
		}
		if (validation != null && validation.contains("|")) {
			final String trueValue = validation.substring(0, validation.indexOf("|"));
			if (string.equals(trueValue)) {
				return true;
			}
		}
		return string.equalsIgnoreCase("yes") || string.equalsIgnoreCase("true");
	}

	public void setValues(ArrayList<SelectableObject> values) {
		this.values = new ArrayList<SelectableObject>();
		if (values != null) {
			for (final SelectableObject so : values) {
				if (so != null) {
					this.values.add(so);
				}
			}
		}
	}

	public String toString4() {
		return "\n        f=new Field(); " + (id != null ? "f.setId(" + id + "); " : "") + (type != null ? "f.setType(\"" + type + "\"); "
				: "") + (name != null ? "f.setName(\"" + name + "\"); " : "") + (cfgkey != null ? "f.setCfgkey(\"" + cfgkey + "\"); "
						: "") + (variable != null ? "f.setVariable(\"" + variable + "\"); " : "") + (validation != null ? "f.setValidation(\"" + validation + "\"); "
								: "") + (value != null ? "f.setValue(\"" + value + "\"); " : "");
	}

	public String toString2() {
		return "\n        Field [id=" + id + ", type=" + type + ", name=" + name + ", cfgkey=" + cfgkey + ", validation=" + validation + ", attributes=" + attributes + ", datatype=" + datatype + ", position=" + position + ", label=" + label + ", helptext=" + helptext + ", length=" + length + ", data=" + data + ", required=" + required + ", value=" + value + ", readonly=" + readonly + ", editable=" + editable + ", relatedField=" + relatedField + ", values=" + values + ", options=" + options + "]";
	}

	@Override
	public String toString() {
		return "\n        Field [" + (id != null ? "id=" + id + ", " : "") + (type != null ? "type=" + type + ", " : "") + (name != null ? "name=" + name + ", "
				: "") + (cfgkey != null ? "cfgkey=" + cfgkey + ", " : "") + (variable != null ? "variable=" + variable + ", " : "") + (validation != null ? "validation=" + validation + ", "
						: "") + (attributes != null ? "attributes=" + attributes + ", " : "") + (datatype != null ? "datatype=" + datatype + ", "
								: "") + (position != null ? "position=" + position + ", " : "") + (label != null ? "label=" + label + ", " : "") + (helptext != null ? "helptext=" + helptext + ", "
										: "") + (url != null ? "url=" + url + ", " : "") + (length != null ? "length=" + length + ", " : "") + (data != null ? "data=" + data + ", " : "") + "required=" + required + ", " + (value != null ? " value class: " + value
														.getClass().getName() + "value=" + value + ", " : "") + "readonly=" + readonly + ", immutable=" + immutable + ", editable=" + editable + ", hidden=" + hidden + ", persist=" + persist + ", " + (relatedField != null ? "relatedField=" + relatedField + ", "
																: "") + (relatedFieldValue != null ? "relatedFieldValue=" + relatedFieldValue + ", " : "") + (values != null ? "values=" + values + ", "
																		: "") + (options != null ? "options=" + options + ", " : "") + (containerPath != null ? "containerPath=" + containerPath + ", "
																				: "") + (callback != null ? "callback=" + callback + ", " : "") + (template != null ? "template=" + template + ", "
																						: "") + (configValueId != null ? "configValueId=" + configValueId + ", " : "") + (acl != null ? "acl=" + acl + ", "
																								: "") + (callbackObject != null ? "callbackObject=" + callbackObject + ", " : "") + (usedEntries != null ? "usedEntries=" + usedEntries
																										: "") + (contexts != null ? "context=" + StringUtils.join(contexts, ",") : "") + "]";
	}

	private SelectableObject findCurrentValue() {

		if (values == null || value == null) {
			return null;
		}
		// Find the current value in the list
		if (value.getClass().equals(SelectableObject.class)) {

			final SelectableObject so = getSoValue();
			for (final SelectableObject so1 : values) {
				// compare by identifier, shortname or longname
				if (so.getIdentifier() != null && so.getIdentifier().equals(so1.getIdentifier())
						|| so.getShortName() != null && so.getShortName().equals(so1.getShortName())
						|| so.getLongName() != null && so.getLongName().equals(so1.getLongName())) {
					return so1;
				}
			}
			for (final SelectableObject so1 : values) {
				if (so.getAttributes() != null && so1.getAttributes() != null) {
					// no luck. see if we can map to an existing entry by any of the attributes (needed if we did not persist the
					// selectable object's identifier but just one of the subattributes
					for (final Map.Entry<String, String> each : so.getAttributes().entrySet()) {
						final String attributeValue = each.getValue();
						if (attributeValue != null && attributeValue.equals(so1.getAttributes().get(each.getKey()))) {
							return so1;
						}
					}
				}
			}
			return so;

		} else {
			final String strvalue = value.toString();
			for (final SelectableObject so1 : values) {
				if (strvalue.equals(so1.getIdentifier())
						|| strvalue.equals(so1.getValue())
						|| strvalue.equals(so1.getShortName())
						|| strvalue.equals(so1.getLongName())) {
					return so1;
				}
			}
			final SelectableObject so = new SelectableObject(strvalue, strvalue, strvalue, new HashMap<String, String>());
			so.setIdentifier(strvalue);
			return so;
		}
	}

	public boolean isFiltered() {
		return filtered;
	}

	/**
	 * Filter the selection list by what's typed in the trigger field.
	 * @param filter
	 */
	public void applyFilter(String filter) {
		filter = StringUtils.trimToEmpty(filter);
		logger.debug("Applying filter " + filter + " for field " + getId());
		if (backupOptions == null && backupValues == null) {
			backup();
		}
		logger.log(NimLog.DEBUG, "Applying filter " + filter);
		if (backupValues != null) {
			final ArrayList<SelectableObject> filteredOptions = new ArrayList<SelectableObject>();
			for (final SelectableObject opt : backupValues) {
				if (opt.contains(filter)) {
					filteredOptions.add(opt);
				}
			}
			setValues(filteredOptions);
			selectCurrentValuesSelectableObject();
		} else if (backupOptions != null) {
			final ArrayList<String> filteredOptions = new ArrayList<String>();
			for (final String opt : backupOptions) {
				if (opt.contains(filter)) {
					filteredOptions.add(opt);
				}
			}
			setOptions(filteredOptions);
		}
		setFiltered(true);
	}

	private void backup() {
		if (options != null) {
			backupOptions = new ArrayList<String>();
			for (final String option : options) {
				backupOptions.add(option);
			}
		}
		if (values != null) {
			backupValues = new ArrayList<SelectableObject>();
			for (final SelectableObject opt : values) {
				backupValues.add(opt);
			}
		}

	}

	private void restore() {
		if (backupOptions != null) {
			options = new ArrayList<String>();
			for (final String option : backupOptions) {
				options.add(option);
			}
		}
		if (backupValues != null) {
			values = new ArrayList<SelectableObject>();
			for (final SelectableObject opt : backupValues) {
				values.add(opt);
			}
		}

	}

	/**
	 * Removes a SelectableObject from the list of {@link #values} by the value of the
	 * SelectableObject.
	 *
	 * @param value
	 *            The value of the SelectableObject to remove
	 */
	public void removeSelectableObjectByValue(final String value) {
		if (values == null) {
			return;
		}
		final Iterator<SelectableObject> iterator = values.iterator();
		while (iterator.hasNext()) {
			final SelectableObject selectableObject = iterator.next();
			if (Objects.equal(value, selectableObject.getValue())) {
				iterator.remove();
			}
		}
	}

	public void removeFilter() {
		restore();
		setFiltered(false);
	}

	/**
	 * Returns whether this field should be visible in the given context.
	 * Will return true if this field has no contexts defined.
	 *
	 * @param context
	 * @return
	 */
	public boolean isVisibleInContext(Context context) {
		if (contexts == null || contexts.length == 0) {
			return true;
		}

		for (final Context c : contexts) {
			if (c != null && c == context) {
				return true;
			}
		}

		return false;
	}

	@Override
	/**
	 * Returns te defined label for this field.
	 * Note: If the field is filtered (i.e. isFiltered() == true), the label will be suffixed with a "  *"
	 */
	public String getLabel() {
		if (isFiltered()) {
			return label + " *";
		}
		return label;
	}
	
	 public Map<String, Object> getAsMap() {
		    final Map<String, Object> map = new HashMap<String, Object>(20);
		    map.put("acl", acl);
		    map.put("name", name);
		    map.put("type", type);
		    map.put("variable", variable);
		    map.put("validation", validation);
		    map.put("datatype", datatype);
		    map.put("position", position);
		    map.put("labelPosition", labelPosition);
		    map.put("label", label);
		    map.put("helptext", helptext);
		    map.put("url", url);
		    map.put("length", length);
		    map.put("numlines", numlines);
		    map.put("data", data);
		    map.put("required", required);
		    map.put("readonly", readonly);
		    map.put("editable", editable);
		    map.put("hidden", hidden);
		    if (relatedField == null || relatedField == 0) {
		      map.put("relatedfield", null);
		    } else {
		      map.put("relatedfield", relatedField);
		    }
		    map.put("relatedfieldValue", relatedFieldValue);
		    if (callback == null || callback == 0) {
		      map.put("callback", null);
		    } else {
		      map.put("callback", callback);
		    }
		    map.put("containerpath", containerPath);
		    map.put("template", template);
		    map.put("immutable", immutable);
		    map.put("defaultvalue", value);
		    map.put("cfgkey", trim(cfgkey));
		    map.put("legacyVariable", legacyVariable);
		    map.put("omitSectionCondition", omitSectionCondition);
		    map.put("salt", trim(salt));
		    map.put("calculation", calculation);
		    map.put("persist", shouldPersist()); // use setter to avoid null values
		    // map.put("contexts", this.contexts == null ? this.contexts : StringUtils.join(this.contexts, ",") + "");
		    if (contexts == null) {
		      map.put("context", contexts);
		    } else {
		      final String[] vals = new String[contexts.length];
		      for (int i = 0; i < contexts.length; i++) {
		        vals[i] = String.valueOf(contexts[i].getValue());
		      }
		      map.put("context", StringUtils.join(vals, ",") + "");
		    }
		    return map;
		  }

}

