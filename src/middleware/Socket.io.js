const sendNotifyRealtime = (io, idUser, notification) => {
  io.emit(`send-notify:${idUser}`, notification);
};
module.exports = {
  sendNotifyRealtime,
};
