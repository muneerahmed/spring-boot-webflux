function fn() {    
  var env = karate.env; // get system property 'karate.env'
  karate.log('karate.env system property was:', env);
  if (!env) {
    env = 'dev';
  }
  var config = {
    env: env
  }
  if (env == 'dev') {
    config.url = 'http://localhost:8080'
  }
  return config;
}