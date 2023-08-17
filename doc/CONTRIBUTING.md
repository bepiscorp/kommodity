# Contributing to Callisto

We would love for you to contribute to Callisto and help make it even better than it is today!
As a contributor, here are the guidelines we would like you to follow:

- [Question or Problem?](#question)
- [Issues and Bugs](#issue)
- [Feature Requests](#feature)
- [Submission Guidelines](#submit)
- [Coding Rules](#rules)
- [Commit Message Guidelines](#commit)


## <a name="question"></a> Got a Question or Problem?

Do not open issues for general support questions
as we want to keep YouTrack issues for bug reports and feature requests.

You've got much better chances of getting your question answered on _#trl-java-world_ Telegram channel. 

Telegram channel is a much better place to ask questions since:
- there are more people willing to help;
- questions and answers stay available for public viewing so your question/answer might help someone else.

To save your and our time, we will systematically close all issues
that are requests for general support and redirect people to Telegram Channel.


## <a name="issue"></a> Found a Bug?

If you find a bug in the source code, you can help us by
[submitting an issue](#submit-issue) to our [YouTrack][youtrack].
Even better, you can [submit a Merge Request](#submit-mr) with a fix.


## <a name="feature"></a> Missing a Feature?
You can *request* a new feature by [submitting an issue](#submit-issue) to our YouTrack.

If you would like to *implement* a new feature, please submit an issue with
a proposal for your work first, to be sure that we can use it. Please consider what kind of change it is:

- For a **Major Feature**, first open an issue and outline your proposal so that it can be
  discussed. This will also allow us to better coordinate our efforts, prevent duplication of work,
  and help you to craft the change so that it is successfully accepted into the project.
- **Small Changes** can be crafted and directly [submitted as a Merge Request](#submit-mr)
  linked to issue [CALLISTO-47](https://yt.cybernation.com/issue/CALLISTO-47).

---

## <a name="submit"></a> Submission Guidelines

### <a name="submit-issue"></a> Submitting an Issue

Before you submit an issue, please search the [issue tracker][youtrack],
there may be an already existing issue for your problem
and the discussion might inform you of workarounds readily available.

We want to fix all the issues as soon as possible, but before fixing a bug we need to reproduce and confirm it.
In order to reproduce bugs, we will systematically ask you to provide a minimal reproduction scenario.

Having a live, reproducible scenario gives us a wealth of important information without asking you additional questions.

If there is no reproducible scenario, share with us at least this info:
- version of Callisto used;
- actual behavior (a use-case that fails);
- expected behavior.

Unfortunately, we are not able to investigate/fix bugs without minimal information,
so if we don't hear back from you, we are going to close an issue that does not have enough info to be reproduced.

You can create new issues by filling [new issue form][youtrack_new_issue].

### <a name="submit-mr"></a> Submitting a Merge Request (MR)

Before you submit your Merge Request (MR) consider the following guidelines:

1. Search [GitLab][gitlab-mrs] for an open or closed MR that relates to your submission.
   You don't want to duplicate effort.
2. Make your changes in a new git branch:

     ```shell
     git checkout -b my-fix-branch develop
     ```

3. Create your patch, **including appropriate test cases**.
4. Follow our [Coding Rules](#rules).
5. Run the full Callisto test suite, as described in the [developer documentation][dev-doc],
   and ensure that all tests pass.
6. Commit your changes using a descriptive commit message that follows our
   [commit message conventions](#commit). Adherence to these conventions
   is necessary because release notes are automatically generated from these messages.

     ```shell
     git commit -a
     ```
   Note: the optional commit `-a` command line option will automatically "add" and "rm" edited files.

7. Push your branch to GitLab:

    ```shell
    git push origin my-fix-branch
    ```

8. In GitLab, send a merge request to `callisto:develop`. <br/>

    ```shell
    git rebase master -i
    git push -f
    ```

9. If we suggest changes then:
    - Make the required updates.
    - Re-run the Callisto test suites to ensure tests are still passing.
    - Rebase your branch and force push to your branch in GitLab repository:

That's it! Thank you for your contribution!


## <a name="rules"></a> Coding Rules

To ensure consistency throughout the source code, keep these rules in mind as you are working:

- All features or bug fixes **must be tested** by one or more unit-tests.
- All public API methods **must be documented**.
- We follow [Kotlin Code Conventions][trl-kt-code-conventions].

---

## <a name="commit"></a> Commit Message Guidelines

We have very precise rules over how our git commit messages can be formatted. 
This leads to **more readable messages** that are easy to follow when looking through the **project history**.
But also, we use the git commit messages to **generate the Callisto change log**.

### Commit Message Format

When committing to Callisto we use [Conventional Commits specification][conventional-commit].

Each commit message consists of:
- a **header**
- an optional **body**
- an optional **footer**

The header has a special format that includes:
- a `type` — see [Commit Type](#commit_type) section
- an optional `scope` — see [Commit Scope](#commit_scope) section 
- a `subject` — general description of changes giving the context starting with uppercase letter;
- an `issue` — issue id in [tracker][youtrack] that commit relates to.

```
<type>(<scope>): <subject>. #<issue>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

We have a few exceptions that do not follow _Conventional Commits specification_:
- header always include issue id;
- Subject always starts with uppercase letter.

Any line of the commit message cannot be longer 120 characters!
This allows the message to be easier to read on GitLab as well as in various git tools.

Examples:

```
docs(release): Update changelog. #CALLISTO-47
```

```
fix!: Perform numerous breaking fixes. #CALLISTO-33

BREAKING CHANGE:

Fix typo in ResponseCode.responseType delegated property
Rename argument of 'process' function 'isMandatory' -> 'mandatory' in 'ExternalValueProcessor'
```

```
revert: This reverts commit c8789170. #CALLISTO-43
```

See more samples in [commits history][gitlab_commit_history] on GitLab.

### Revert commit

If the commit reverts a previous commit, it should begin with `revert: `,
followed by the header of the reverted commit.
In the body it should say: `This reverts commit <hash>. #CALLISTO-XXX`,
where the hash is the SHA of the commit being reverted.

### <a name="commit_type"></a> Commit Type
Must be one of the following:

- **build** — Changes that affect the build system or external dependencies (example scopes: gradle, docker, Jenkins)
- **ci** — changes to our CI configuration files and scripts (example scopes: Jenkins, GitLab)
- **docs** — documentation only changes
- **feat** — a new feature
- **fix** — a bug fix
- **perf** — A code change that improves performance
- **refactor** — A code change that neither fixes a bug nor adds a feature
- **style** — Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
- **test** — Adding missing tests or correcting existing tests

### <a name="commit_scope"></a> Commit Scope
The scope should be the short name of the module affected.

The following is the list of supported scopes:

- **api** - relates to _api-commons_
- **app** - relates to _app-commons_
- **base** - relates to _base-commons_
- **blob** - relates to _blob-commons/blob-commons-core_
- **blob-filesystem** - relates to _blob-commons/blob-filesystem-extension_
- **blob-hibernate** - relates to _blob-commons/blob-hibernate-extension_
- **gradle** - relates to _callisto-gradle_
- **gradle-kt** - relates to Kotlin plugins in _callisto-gradle/plugins_
- **data** - relates to _data-commons_
- **hibernate** - relates to _hibernate-commons_
- **signal** - relates to _signal-commons/signal-commons-core_
- **signal-unix** - relates to _signal-commons/signal-commons-unix_
- **test** - relates to _test-commons_

There are currently a few exceptions to the "use package name" rule:

- **catalog** - relates to _gradle/libs.versions.toml_
- **package** — used for changes that change the package layout in all of our packages,
  public path changes, changes to bundles, etc
- **release** — changes related to release routines
- none/empty string: useful for `style`, `test` and `refactor` changes that are done across all packages (e.g. `style: add missing semicolons`)

### Commit Subject

The subject contains a short description of the change:

- use the imperative, present tense: "change" not "changed" nor "changes";
- capitalize the first letter;
- do not forget to end subject with dot character and with an id of the related issue.

### Commit Body

Just as in the `subject`, use the imperative, present tense: "change" not "changed" nor "changes".
The body should include the motivation for the change and contrast this with previous behavior.

### Commit Footer

The footer should contain any information about **Breaking Changes**.
It is also the place to reference commit hash for `revert` commits.

**Breaking Changes** should start with the word `BREAKING CHANGE:` with a space or two newlines
The rest of the commit message is then used for this.


[tg-mikhail-gostev]: https://t.me/pihanya
[youtrack]: https://yt.cybernation.com/issues/CALLISTO
[youtrack_new_issue]: https://yt.cybernation.com/newIssue?project=CALLISTO
[gitlab]: https://gitlab.cybernation.com/cybernation/callisto
[gitlab_commit_history]: https://gitlab.cybernation.com/cybernation/callisto/-/commits/develop/
[gitlab-mrs]: https://gitlab.cybernation.com/cybernation/callisto/-/merge_requests
[trl-kt-code-conventions]: https://wiki.cybernation.com/index.php/Kotlin_Code_Conventions
[conventional-commit]: https://www.conventionalcommits.org/en/v1.0.0/#summary
[dev-doc]: https://gitlab.cybernation.com/cybernation/callisto/-/blob/develop/docs/DEVELOPER.md

