The only way I could get the WordCountITCase.scala integration test file to run
was to rename it to WordCountITCaseTest.scala

That's not ideal, because we *should* be able to separate integration tests from unit tests.

I'd love to be able to create a 027_run_integration_tests.sh script, too.

Here's a stack overflow that should be able to help, later:

https://stackoverflow.com/questions/1399240/how-do-i-get-my-maven-integration-tests-to-run
