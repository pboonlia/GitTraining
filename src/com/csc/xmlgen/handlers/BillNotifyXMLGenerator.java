package com.csc.xmlgen.handlers;

import org.w3c.dom.Document;

import com.csc.xmlgen.util.XmlUtility;

public class BillNotifyXMLGenerator {
	String rqUID;
	String policyNbr;
	String accountNbr;
	String cltLastName;
	String zipCode;
	int zip=29223;
	int xmlCount;
	Document sourceDom = null;
	String destFilePath = "";
	
	String agencyNbr;

	public BillNotifyXMLGenerator(int xmlCount, String rqUID, String policyNbr,
			String accountNbr, String cltLastName, String zipCode, String agencyNbr) {
		this.xmlCount = xmlCount;
		this.rqUID = rqUID;
		this.policyNbr = policyNbr;
		this.accountNbr = accountNbr;
		this.cltLastName = cltLastName;
		this.zipCode = zipCode;
		this.agencyNbr=agencyNbr;
	}

	public String getDestFilePath() {
		return destFilePath;
	}

	public void setDestFilePath(String destFilePath) {
		this.destFilePath = destFilePath;
	}

	public void process(String filePath) {

		sourceDom = XmlUtility.readXMLfromFile(filePath);
		for (int count = 0; count < xmlCount; count++) {
			Document destDom = null;
			String genRqUID = generateRqUID(rqUID, count);
			destDom = XmlUtility.updaterqUIDbyIndex(sourceDom, "RqUID",
					genRqUID);
			destDom = XmlUtility.updateXmlTagbyIndex(sourceDom, "PolicyNumber",
					policyNbr + count);
			//destDom = XmlUtility.updateXmlTagbyIndex(sourceDom,
			//		"AccountNumberId", accountNbr + count);
			destDom = XmlUtility.updateAccountNumberTagbyIndex(sourceDom,
					"AccountNumberId", accountNbr + count);
			
			destDom = XmlUtility.updaterqUIDbyIndex(sourceDom, "Surname",
					cltLastName + count);
			//zip=zip+count;
			destDom = XmlUtility.updateZipTagbyIndex(sourceDom, "PostalCode",zip);
			//Agency Code Starts
			/*destDom = XmlUtility.updateAgentTagbyIndex(sourceDom, "ContractNumber",
					new RandomAgentGenerator().getAgencyNbr(count));
*/			//Agency Code Ends

			XmlUtility.saveXMLtoFile(destDom, destFilePath + "//" + genRqUID
					+ ".xml");
		}
	}

	private String generateRqUID(String rqUID, int count) {
		// TODO Auto-generated method stub
		rqUID = rqUID.substring(0, 29) + String.format("%07d", count);
		return rqUID;
	}

}
