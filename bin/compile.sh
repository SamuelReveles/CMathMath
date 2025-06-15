#! /bin/bash

directory="$(dirname "${BASH_SOURCE[0]}")"
cd $directory


jflex ./CMathMath.flex
java -jar ./java-cup-11b.jar -parser CMathMathParser ./CMathMath.cup