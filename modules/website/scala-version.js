const fs = require('fs')
const build = fs.readFileSync('../../build.sbt', 'utf8')
const scalaVersion = '3.3.0';

console.log('detected scala version', scalaVersion)
module.exports = scalaVersion