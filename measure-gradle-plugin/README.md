# JMeasure Gradle plugin

## Usage

```gradle
jmeasure {
	instrumentSet {
		//from external file
		fromJson 'instruments.json'
		//or hardcode into buildscript
		vxi11 '192.168.1.50', 'inst0'
		//or discover
		lxiDiscover (
			'wavegen': 'SDG1032X'
		)
	}
	tests {
		
	}
}
```
