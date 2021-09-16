### Notes on parallelism

From https://ci.apache.org/projects/flink/flink-docs-master/docs/concepts/flink-architecture/#task-slots-and-resources

> A Flink cluster needs exactly as many task slots as the highest parallelism
> used in the job. No need to calculate how many tasks (with varying
> parallelism) a program contains in total.

This job had parallelism set to 4.  I reduced it to 1 on main branch, just so
it would run.  If the parallelsim was set to 4, like it is in this branch, then
it just kinda sits there spinning.

My current suspicion is that I need to set the 
`taskmanager.numberOfTaskSlots: 4`, and that I may need to bump up the memory
to allow for that to work.  So far, no luck... getting an exception that reads:

> Caused by: java.util.concurrent.CompletionException:
> org.apache.flink.runtime.jobmanager.scheduler.NoResourceAvailableException:
> Slot request bulk is not fulfillable! Could not allocate the required slot
> within slot request timeout

Found this Stack Overflow with relevant details:
https://stackoverflow.com/questions/60619913/flink-job-unfulfillableslotrequestexception-could-not-fulfill-slot-req-req-re

