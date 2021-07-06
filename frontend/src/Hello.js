import { useKeycloak } from '@react-keycloak/web';
import React, {useState} from 'react';

const Hello = () => {
  const [result, setResult] = useState('');

  
  const keyCloak = useKeycloak();

  function getAdminHello() {
    fetch("http://localhost:8080/admin/hello", {
      method: "GET",
      headers: { 
        'Authorization': `Bearer ${keyCloak.keycloak.token}`//,
      },
    })
      .then(res => res.text())
      .then(text => setResult(text));
  }

  function getRegularnHello() {
    fetch("http://localhost:8080/hello", {
      method: "GET",
      headers: { 
        'Authorization': `Bearer ${keyCloak.keycloak.token}`//,
      },
    })
      .then(res => res.text())
      .then(text => setResult(text));
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column'}}>
      <p style={{ flex: '1 100%'}}>Result: {result}</p>
      <div style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
        <button style={{ fontSize: 50 }} onClick={getAdminHello} >Get Admin hello</button>
        <button style={{ fontSize: 50 }} onClick={getRegularnHello} >Get Regular hello</button>
      </div>
    </div>
  );

}

export default Hello;