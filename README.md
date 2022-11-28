# Generation of Embedded Real-Time Code based on Aspects (GenERTiCA)

GenERTiCA is a tool created to support the Aspect-oriented Model Driven Engineering for Real-Time systems (AMoDE-RT).  
It uses a set of mapping rules scripts to transform a UML model into source code for a given target platform.  
For that, the Distributed Embedded Real-time Compact Specification (DERCS) model is used.   
Currently, GenERTiCA is capable of generating not only source code for software components, but also for hardware components using Hardware Description Languages (HDL).   
Furthermore, GenERTiCA identifies whether aspects of the Distributed Embedded Real-time Aspects Framework (DERAF) have been specified within UML models, and then weaves aspects' code into the generated code, realizing the handling of system's non-functional requirements using the chosen target platform.   

## Cloning
When cloning the repository, use `git clone --recursive [REPO_URL]` to also initialize the submodule.

If you have already cloned without doing this, the submodule can also be initialized after the fact by running  
`git submodule update --init`.

## Building
Run `./gradlew build` (or `gradlew.bat build` for the windows command prompt) in a terminal.  
The built distribution can be found in the `build/distributions/` directory.

## Running
To generate source code for an Eclipse Papyrus UML model,
run GenERTiCA using the following command:  
`./GenERTiCA [Path to model .uml file] [Path to mapping rules .xml] [Path of output directory]`  
(or `GenERTiCA.bat` when using the windows command promt)

The executable to run can be found in the `bin/` directory of a built
GenERTiCA distribution. (See [Building](#building))

## Demo
An example UML model to try GenERTiCA can be found here:   
https://github.com/lesc-utfpr/amode-rt-case-studies/tree/master/uml-models-papyrus/Wheelchair  
The mapping rules passed to GenERTiCA should be the included file  `MappingRules/MappingRules_Java.xml`.

(See [Running](#running) for how information on how to invoke GenERTiCA.)