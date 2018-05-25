#!/bin/bash
set -e

EXIT_STATUS=0

git config --global user.name "$GIT_NAME"
git config --global user.email "$GIT_EMAIL"
git config --global credential.helper "store --file=~/.git-credentials"
echo "https://$GH_TOKEN:@github.com" > ~/.git-credentials

./gradlew main:test || EXIT_STATUS=$?

if [[ $EXIT_STATUS -ne 0 ]]; then
    echo "Main Test failed"
    exit $EXIT_STATUS
fi

./gradlew build || EXIT_STATUS=$?

if [[ $EXIT_STATUS -ne 0 ]]; then
    echo "Project Build failed"
    exit $EXIT_STATUS
fi

# Publish Main site
if [[ $TRAVIS_BRANCH == 'master' && $TRAVIS_PULL_REQUEST == 'false' ]]; then

    ./gradlew main:runShadow || EXIT_STATUS=$?

    if [[ $EXIT_STATUS -ne 0 ]]; then
        echo "Main Website generation failed"
        exit $EXIT_STATUS
    fi

	git clone https://${GH_TOKEN}@github.com/$TRAVIS_REPO_SLUG.git -b gh-pages gh-pages --single-branch > /dev/null
	cd gh-pages
	cp -r ../main/build/site/* .
    if git diff --quiet; then
        echo "No changes in MAIN Website"
    else
        git add -A
	    git commit -a -m "Updating main Micronaut site for Travis build: https://travis-ci.org/$TRAVIS_REPO_SLUG/builds/$TRAVIS_BUILD_ID"
	    git push origin HEAD
    fi

	cd ..
	rm -rf gh-pages

fi

# Publish Guides site
if [[ $TRAVIS_BRANCH == 'master' && $TRAVIS_PULL_REQUEST == 'false' ]]; then

    ./gradlew guides:runShadow || EXIT_STATUS=$?

    if [[ $EXIT_STATUS -ne 0 ]]; then
        echo "Guides Website generation failed"
        exit $EXIT_STATUS
    fi

    git clone https://${GH_TOKEN}@github.com/micronaut-projects/micronaut-guides.git -b gh-pages gh-pages --single-branch > /dev/null
	cd gh-pages
	cp -r ../guides/build/site/* .
	if git diff --quiet; then
        echo "No changes in GUIDES Website"
    else
	    git add -A
	    git commit -a -m "Updating guides site for Travis build: https://travis-ci.org/$TRAVIS_REPO_SLUG/builds/$TRAVIS_BUILD_ID"
	    git push origin HEAD
	fi
	cd ..
	rm -rf gh-pages
fi
