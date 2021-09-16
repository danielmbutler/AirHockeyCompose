const { Server } = require("socket.io");

const io = new Server({ /* options */ });

io.on("connection", (socket) => {
  console.log("connection")
  console.log(socket)
});

io.listen(3000);

