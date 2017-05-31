/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.text.MessageFormat;

/**
 * Cheater Cheater Pumpkin Eater.
 * This class makes a cheatsheet-type table.
 * @author nonfrt
 */
public class PumpkinEater {
    private static final String htmlTemplate = "<div><h1>{0}</h1><table>{1}{2}</table><style>{3}</style></div>";
    private static final String headerTemplate = "<thead><tr>{0}</tr></thead>";
    private static final String headerEntryTemplate = "<th>{0}</th>";
    private static final String bodyTemplate = "<tbody>{0}</tbody>";
    private static final String rowTemplate = "<tr>{0}</tr>";
    private static final String rowEntryTemplate = "<td>{0}</td>";
    private static final String cssTemplate = "";
    
    private Integer numOfColumns;
    private String html;
    
    public PumpkinEater(String title,String[] headers,String[]... data) {
        numOfColumns = headers.length;
        String header = getHeader(headers);
        String body = getBody(data);
        String css = getCss();
        html = MessageFormat.format(htmlTemplate, title,header,body,css);
    }
    
    private String getCss() {
        String css = "div{overflow-x:auto;}"+
                "table{border-collapse: collapse;width: 100%;}"+
                "tr:nth-child(even){background-color: #f2f2f2}"+
                "th,td{text-align: left;padding: 8px;}"+
                "th{background-color: #4CAF50;color: white;}";
        return css;
    }
    
    private String getHeader(String[] headers) {
        String header = "";
        for (String head : headers) {
            header += MessageFormat.format(headerEntryTemplate, head);
        }
        header = MessageFormat.format(headerTemplate, header);
        return header;
    }
    
    private String getBody(String[][] rows) {
        String body = "";
        for (String[] data : rows) {
            String row = "";
            for (String datum : data) {
                row += MessageFormat.format(rowEntryTemplate, datum);
            }
            body += MessageFormat.format(rowTemplate, row);
        }
        body = MessageFormat.format(bodyTemplate, body);
        return body;
    }
    
    public String getHtml() {
        return html;
    }
}
