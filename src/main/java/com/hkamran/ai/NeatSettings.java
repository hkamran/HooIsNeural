package com.hkamran.ai;

public class NeatSettings extends NetworkSettings {
	
	public double addConnectionChange = 0.05;
	public double removeConnectionChance = 0.1;
	public double addNodeChance = 0.5;
	public double adjustWeightChance = 0.15;
	public double addLayerChance = 0.01;
	
	int maxMutations = 2;
	double weightMutationStep = 2;
	int hiddenNodeCap = 3;
	int hiddenLayerCap = 2;
	double adjustmentChange = 0.2;
	
	public static NeatSettings create() {
		return new NeatSettings();
	}

}