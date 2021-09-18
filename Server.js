
const SocketServer = require('websocket').server
const http = require('http')

const server = http.createServer((req, res) => {})

server.listen(3000, ()=>{
    console.log("Listening on port 3000...")
})

wsServer = new SocketServer({httpServer:server})

const connections = []
const players = []

function sendPairingMessage(){
  connections.forEach(element => {
    element.sendUTF( "paired : " + JSON.stringify(players, null, 2))
  })
}

function sendPlayerDetails(player , connection){
  connections.forEach(element => {
        
    if (element = connection)
        console.log("sending message")
        element.sendUTF("connection confirmed : " +  JSON.stringify(player
        ))
})
}
function  getUniqueID() {
  function s4() {
      return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
  }
  return s4() + s4() + '-' + s4();
};


wsServer.on('request', (req) => {
    const connection = req.accept()
    console.log('new connection')
   
      // only allow 2 player session
    if(connections.length <= 1){
      connections.push(connection)
    }
      

    connection.on('message', (mes) => {
    
      if(JSON.parse(mes.utf8Data).name){
        if(players.length < 2){
               const player = {
          name: JSON.parse(mes.utf8Data).name,
          index: connections.indexOf(connection),
          playerID: getUniqueID()

        }
        players.push(player)
        sendPlayerDetails(player, connection)
        console.table("players: " + JSON.stringify(players, null, 2))
        // setup pairing if we have more than 1 player
        if(players.length == 2 && connections.length == 2)  sendPairingMessage()
        }
     
      } else { connections.forEach(element => {
        
        if (element != connection)
            console.log("sending message")
            element.sendUTF(mes.utf8Data)
    })}
   
       
    })
   

    connection.on('close', (resCode, des) => {
        console.log('connection closed')
        connections.splice(connections.indexOf(connection), 1)
    })

})

