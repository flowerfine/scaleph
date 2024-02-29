import { io } from "socket.io-client";

export const SqlSocket = io("http://localhost:8085/sql", {
  transports: ["websocket"],
});
