Guidelines for contributing to Hibernate Validator
====

Contributions from the community are essential in keeping Hibernate Validator strong and successful.
This guide focuses on how to contribute back to Hibernate Validator using GitHub pull requests.
If you need help with cloning, compiling or setting the project up in an IDE please refer to
[this page](https://hibernate.org/validator/contribute/).

## Legal

All original contributions to Hibernate Validator are licensed under the
[Apache License version 2.0](https://www.apache.org/licenses/LICENSE-2.0),
or, if another license is specified as governing the file or directory being
modified, such other license. The Apache License text is included verbatim in the [license.txt](license.txt) file
in the root directory of the repository.

All contributions are subject to the [Developer Certificate of Origin (DCO)](https://developercertificate.org/).
The DCO text is also included verbatim in the [dco.txt](dco.txt) file in the root directory of the repository.

## Getting Started

If you are just getting started with Git, GitHub and/or contributing to Hibernate Validator there are a
few prerequisite steps:

* Make sure you have a [Hibernate JIRA account](https://hibernate.atlassian.net)
* Make sure you have a [GitHub account](https://github.com/signup/free)
* [Fork](https://help.github.com/articles/fork-a-repo/) the Hibernate Validator [repository](https://github.com/hibernate/hibernate-validator).
As discussed in the linked page, this also includes:
    * [Setting](https://help.github.com/articles/set-up-git/) up your local git install
    * Cloning your fork
* Instruct git to ignore certain commits when using `git blame`:
  ```
  git config blame.ignoreRevsFile .git-blame-ignore-revs
  ```

## Create a test case

If you have opened a JIRA issue but are not comfortable enough to contribute code directly, creating a self
contained test case is a good first step towards contributing.

As part of our efforts to simplify access to new contributors, we provide [test case templates for the Hibernate family
projects](https://github.com/hibernate/hibernate-test-case-templates).

Just fork this repository, build your test case and attach it as an archive to a JIRA issue.

## Create a topic branch

Create a "topic" branch on which you will work.  The convention is to name the branch
using the JIRA issue key.  If there is not already a JIRA issue covering the work you
want to do, create one.  Assuming you will be working from the main branch and working
on the JIRA HV-123 :

     git checkout -b HV-123 main


## Code

Code away...

## Formatting rules and style conventions

The project build comes with preconfigured plugins that apply automatic formatting/sorting of imports and
a set of other checks. Running a simple build should automatically apply all the formatting rules and checks:

```shell
mvn clean verify
```

Alternatively, if only applying the formatting is required, you could run the next command:

```shell
mvn spotless:apply checkstyle:check
```

---
**NOTE**: running the above command requires the `org.hibernate.validator:hibernate-validator-build-config`
being available. If it is a first time building the project you may need to execute:
```shell
mvn clean install -am -pl build/build-config
```
---

The project comes with formatting files located in:
- [hibernate_validator_style.xml](build/build-config/src/main/resources/hibernate_validator_style.xml)
- [hibernate_validator_style.importorder](build/build-config/src/main/resources/hibernate_validator_style.importorder)

These files can be used in the IDE if applying formatting as-you-code within the IDE is something you'd prefer.

## Commit

* Make commits of logical units.
* Be sure to start the commit messages with the JIRA issue key you are working on. This is how JIRA will pick
up the related commits and display them on the JIRA issue.
* Avoid formatting changes to existing code as much as possible: they make the intent of your patch less clear.
* Make sure you have added the necessary tests for your changes.
* Run _all_ the tests to assure nothing else was accidentally broken:

```shell
mvn verify
```

_Prior to committing, if you want to pull in the latest upstream changes (highly
appreciated by the way), please use rebasing rather than merging (see instructions below).  Merging creates
"merge commits" that really muck up the project timeline._

Add the original Hibernate Validator repository as a remote repository called upstream:
```shell
git remote add upstream https://github.com/hibernate/hibernate-validator.git
```

If you want to rebase your branch on top of the main branch, you can use the following git command:
```shell
git pull --rebase upstream main
```

## Submit

* Push your changes to a topic branch in your fork of the repository.
* Initiate a [pull request](https://help.github.com/articles/creating-a-pull-request-from-a-fork/).
* Update the JIRA issue, adding a comment including a link to the created pull request.
