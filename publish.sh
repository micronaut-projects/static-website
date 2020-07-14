#!/bin/bash
set -e

EXIT_STATUS=0

git config --global user.name "$GIT_NAME"
git config --global user.email "$GIT_EMAIL"
git config --global credential.helper "store --file=~/.git-credentials"
echo "https://$GH_TOKEN:@github.com" > ~/.git-credentials

./gradlew build || EXIT_STATUS=$?

if [[ $EXIT_STATUS -ne 0 ]]; then
    echo "Project Build failed"
    exit $EXIT_STATUS
fi

# Publish Main site
git clone https://${GH_TOKEN}@github.com/micronaut-projects/static-website.git -b gh-pages gh-pages --single-branch > /dev/null
cd gh-pages
cp -r ../build/dist/* .
if git diff --quiet; then
  echo "No changes in MAIN Website"
else
  git add -A
  git commit -a -m "Updating main Micronaut site for Github Actions run:$GITHUB_RUN_ID"
  git push origin HEAD
fi

cd ..
rm -rf gh-pages

exit $EXIT_STATUS