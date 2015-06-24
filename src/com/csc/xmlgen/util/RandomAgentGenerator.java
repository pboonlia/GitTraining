package com.csc.xmlgen.util;

import java.util.ArrayList;

public class RandomAgentGenerator {

	private ArrayList<String> agentList = new ArrayList<String>();

	public RandomAgentGenerator() {

		int count = 0;

		while (count < 25) {
			String agentNbr = "0000";
			int agtInit = 4300;
			agtInit = agtInit + count;
			agentList.add(agentNbr = agentNbr + agtInit);
			count++;
		}

	}

	public String getAgencyNbr(int count) {

		int agentListSize = agentList.size();

		String agency = agentList.get(count % agentListSize);

		return agency;

	}

}
