
const SocketServer = require('websocket').server
const http = require('http')

const server = http.createServer((req, res) => {})

server.listen(3000, ()=>{
    console.log("Listening on port 3000...")
})

wsServer = new SocketServer({httpServer:server})

const connections = []
const players = []

wsServer.on('request', (req) => {
    const connection = req.accept()
    console.log('new connection')
   
      connections.push(connection)

      if(connections.length > 1){
        connections.forEach(element => {
          element.sendUTF("paired")
          element.sendUTF(players.toString())
        })
      }


    connection.on('name', (name) => {
      const player = {
        name: name,
        index: connections.indexOf(connection)
      }
      players.push(player)
      console.log("players: " + players)
    })
    
   
    connection.on('message', (mes) => {
    console.log(mes)
   
        connections.forEach(element => {
            if (element != connection)
                console.log("sending message")
                element.sendUTF(mes.utf8Data)
        })
    })

    connection.on('close', (resCode, des) => {
        console.log('connection closed')
        connections.splice(connections.indexOf(connection), 1)
    })

})

