import { customError } from "./CustomError.js";
import {Notification} from "../models/Notification.js";
const createNotification = async (notify) => {
  try {
    let notifyFound_list;
    if (notify.idPost) {
      notifyFound_list = await Notification.find({ idPost: notify.idPost });
    } else {
      notifyFound_list = await Notification.find({ idBlog: notify.idBlog });
    }
    if (notifyFound_list) {
      let notifyUpdate = null;
      notifyFound_list.forEach((item) => {
        if (item.content == notify.content) {
          notifyUpdate = item;
          if (notify.statusPost) {
            notifyUpdate.statusPost = notify.statusPost;
          } else if (notify.statusBlog) {
            notifyUpdate.statusBlog = notify.statusBlog;
          }
          notifyUpdate.created_on = Date.now();
          notifyUpdate.status = "new";
          return;
        }
      });

      if (notifyUpdate) {
        await Notification.findByIdAndUpdate(notifyUpdate._id, notifyUpdate);
        return notifyUpdate;
      } else {
        await new Notification(notify).save();
        return notify;
      }
    } else {
      await new Notification(notify).save();
      return notify;
    }
  } catch (error) {
    return error;
  }
};

export { createNotification };
