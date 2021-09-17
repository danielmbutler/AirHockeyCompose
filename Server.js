
const SocketServer = require('websocket').server
const http = require('http')

const server = http.createServer((req, res) => {})

server.listen(3000, ()=>{
    console.log("Listening on port 3000...")
})

wsServer = new SocketServer({httpServer:server})

const connections = []

wsServer.on('request', (req) => {
    const connection = req.accept()
    console.log('new connection')
    if(connections.length < 2){
      connections.push(connection)
    } else console.log("max connections")
    console.log(connections)
    connection.on('message', (mes) => {
    console.log(mes)
        connections.forEach(element => {
            if (element != connection)
                element.sendUTF(mes.utf8Data)
        })
    })

    connection.on('close', (resCode, des) => {
        console.log('connection closed')
        connections.splice(connections.indexOf(connection), 1)
    })

})

