# HooIsNeural
A Java implementation of NEAT, Feed-Forward, and back-propagating neural networks.

## Screenshots
<div align="center">
		<img src="https://raw.githubusercontent.com/hkamran/HooIsNeural/master/images/neat_xor_2.gif"></img>
		<img src="https://raw.githubusercontent.com/hkamran/HooIsNeural/master/images/neat_xor_3.gif"></img>
</div>
<div align="center">
        <img src="https://raw.githubusercontent.com/hkamran/HooIsNeural/master/images/neat_xors_1.gif"></img>		
</div>

## Documentation

### Back-Propogating Network

#### Creating:
Use the network builder class to generate a back-propogating network.
```java
NetworkBuilder
	.create(NetworkType.BACKPROP)
	.setLabel("XOR GATE")
	.setInputLayer(
			LayerBuilder
			.create()
			.addNodes(2, Activations.sigmoid))
	.addHiddenLayer(
			LayerBuilder
			.create()
			.addNodes(4, Activations.sigmoid))
	.setOutputLayer(
			LayerBuilder
			.create()
			.addNodes(1, Activations.sigmoid)
			)
	.withBiasNode()
	.withVisualizer()
	.withSettings(
		BackPropSettings
		.create()
		.setLearningRate(0.04)
		.setRandomSeed(3000))
	.createAllConnections()
	.build();	
```
#### Training:
To train the network for 1 cycle execute the following code.
```java
network.clear();
network.train();
```

#### Executing: 
To get the output execute the following code.
```java
network.clear();
network.setInput(input);
network.calculate();
network.getOutput();
```


### NEAT Network

#### Creating:
Using the network builder class we can also create a NEAT network.
```java
NetworkBuilder
	.create(NetworkType.EVOLUTION)
	.setLabel("XOR GATE")
	.setInputLayer(
			LayerBuilder
			.create()
			.addNodes(2, Activations.sigmoid))
	.addHiddenLayer(						
			LayerBuilder
			.create()
			.addNodes(0, Activations.sigmoid))
	.setOutputLayer(
			LayerBuilder
			.create()
			.addNodes(1, Activations.sigmoid)
			)
	.withBiasNode()
	.withSettings(
		NeatSettings
		.create()
		.setRandomSeed(3000)
		.setHiddenNodeCap(4)
		.setHiddenLayerCap(1)
		.setMaxMutations(5)
		.setWeightAdjustment(1)
		.setMaxWeight(10.0)
		.setMinWeight(-10.0)
		.setPopulationSize(500)
		.setActivation(Activations.sigmoid))
	.build();	
```
#### Training:
First we implement the NeatFitness class to determine the fitness of each network during the evolution process. In this case, 
it is the XOR function.
```java
public class XORFitness implements NeatFitness {

	@Override
	public double calculate(Network network) {
		double[][] inputs = new double[][] {{1, 1}, {1, 0}, {0, 1}, {0, 0}};
		double[][] outputs = new double[][] {{0}, {1}, {1}, {0}};

		double score = 0;
		for (int i = 0; i < inputs.length; i++) {
			double[] input = inputs[i];
			double[] output = outputs[i];
			network.clear();
			network.setInput(input);
			network.calculate();

			double[] actual = network.getOutput();
			double err = Math.abs(output[0] - actual[0]);
			score += output[0] == 0 ? 1.0 - err : 1.0 - (err * 2);
		}

		return score;
	}
}
```

Next we apply the fitness function to the network and we begin the evolution process.

```java
network.setFitness(new XORFitness());
int expectedFitness = 3.5;
int generation = 0;
while (network.getFitness() < expectedFitness) {
	network = network.train();
	generation++;
}
```

#### Executing:
We first clear any existing input and then set ours, and execute the feed-forward process to get the output.
```java
network.clear();
network.setInput(new double[] {1,1});
network.calculate();
network.getOutput();
```

## Requirements

 - Java 8+
 - Maven 1.3+
