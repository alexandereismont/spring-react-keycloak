import React from 'react'
import logo from './logo.svg';
import './App.css';
import Keycloak from 'keycloak-js'
import { ReactKeycloakProvider } from '@react-keycloak/web'
import Hello from './Hello';

const keycloak = new Keycloak({
  url: `http://localhost:8180/auth`,
  realm: "SpringBootCloak",
  clientId: "spring-boot-app",
})
const initOptions = { pkceMethod: 'S256',  onLoad: 'login-required'}

const LoadingComponent = () => {
  return (<h1>Loading</h1>);
}

const handleOnEvent = async (event, error) => {
  console.log('Event : ' + event);
  if (event === 'onAuthSuccess') {
    if (keycloak.authenticated) {
      
    }
  }
}

const onEvent = async (event, error) => {
  console.log('Event : ' + event);
  if (event === 'onAuthSuccess') {
    if (keycloak.authenticated) {
      
    }
  }
}

function App() {
  return (
    <ReactKeycloakProvider
      authClient={keycloak}
      initOptions={initOptions}
      handleOnEvent={handleOnEvent}
      onEvent={onEvent}
      LoadingComponent={<LoadingComponent />}
    >
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            Edit <code>src/App.js</code> and save to reload.
          </p>
          <Hello />
        </header>
      </div>
    </ReactKeycloakProvider>
  );
}

export default App;
