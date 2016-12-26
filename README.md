[![Apache license](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg)](http://opensource.org/licenses/Apache2.0)
[![Build Status](https://travis-ci.org/robstoll/minimalist.svg?branch=master)](https://travis-ci.org/robstoll/minimalist)
[![Coverage](https://codecov.io/github/robstoll/minimalist/coverage.svg?branch=master)](https://codecov.io/github/robstoll/minimalist?branch=master)

# Minimalist
A large code base results in a large test set if always all tests need to be run when one does a change.
Yet, not all tests cover test cases which are effectively affected by a change. 
The minimalist (this library) shall help to reduce the time needed to verify changes against a test set
but still keep up a good coverage of affected test cases by calculating an ideal minimal test set.