package com.hkamran.ai;

public class NeatSettings extends NetworkSettings {
	
	double createConnectionChance = 0.05;
	double removeConnectionChance = 0.1;
	double createNodeChance = 0.02;
	double modifyWeightChance = 0.15;
	double createHiddenLayerChance = 0.01;
	
	int maxMutations = 2;
	double weightMutationStep = 2;
	int hiddenNodeCap;
	int hiddenLayerCap;
	
}