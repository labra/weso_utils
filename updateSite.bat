rm -rf latest/api/*
cp -R /src/shapes/weso_utils/target/scala-2.11/api/* latest/api
git add -A
git commit -m "Updated site"
git push