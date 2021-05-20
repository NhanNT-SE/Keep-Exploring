const sendNotifyRealtime = (io, idUser, notification) => {
  io.emit(`send-notify:${idUser}`, notification);
};

export { sendNotifyRealtime };
