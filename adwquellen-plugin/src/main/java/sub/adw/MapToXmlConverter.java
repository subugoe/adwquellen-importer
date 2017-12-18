package sub.adw;

import java.util.Map;

import com.google.common.collect.ListMultimap;

public class MapToXmlConverter {

	public String convertToSolrXml(ListMultimap<String, String> fieldMap) {
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		xml.append("<add><doc>\n");

		for (String key : fieldMap.keySet()) {
			for (String value : fieldMap.get(key)) {
				if (!"".equals(value)) {
					xml.append("<field name=\"" + key + "\"><![CDATA[" + value + "]]></field>\n");
				}
			}
		}
		xml.append("</doc></add>");
		return xml.toString();
	}
}
