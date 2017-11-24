package sub.adw;

import java.util.Map;

public class MapToXmlConverter {

	public String convertToSolrXml(Map<String, String> fieldMap) {
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		xml.append("<add><doc>\n");

		for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
			xml.append("<field name=\"" + entry.getKey() + "\"><![CDATA[" + entry.getValue() + "]]></field>\n");
		}
		xml.append("</doc></add>");
		return xml.toString();
	}
}
