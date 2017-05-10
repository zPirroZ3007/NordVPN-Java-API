# NordVPN-API
Just a simple Java API that permits to call NordVPN API

It simply calls the api and dumps the JSON back.

## Usage
```javascript
import nordapi.NordAPI;

NordAPI api = new NordAPI();

String nameServers = api.nameServers();
// Do other stuff
```

Examples can be found in `src/examples/`
