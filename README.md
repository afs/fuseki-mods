Make a module.

A module does not include jena-fuseki-main or apache-jena-libs:pom.

A pair of maven modules:

custom-module
custom-fmod


custom-module is the implementation with dependencies.
It is testable

custom-fmod depends on custom-module only and excludes jena-fuseki-main or
apache-jena-libs:pom
It also includes the choice of logging implementation 

Test example for jena-modEx-module

See 

jena-modEd-fmod
jena-modEx-module

Simple cases: just tweaking Fuseki 

Can be combined.
Harder to test.
