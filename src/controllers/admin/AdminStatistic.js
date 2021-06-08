import fs from "fs";
import { Post } from "../../models/Post.js";
import { User } from "../../models/User.js";
import { customError } from "../../helpers/CustomError.js";
import { customResponse } from "../../helpers/CustomResponse.js";
const monthList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

const statisticsNumber = async (req, res, next) => {
  try {
    const totalPost = await Post.countDocuments({});
    const pendingPost = await Post.countDocuments({ status: "pending" });
    const need_updatePost = await Post.countDocuments({
      status: "need_update",
    });
    const need_updateBlog = await Blog.countDocuments({
      status: "need_update",
    });
    const donePost = await Post.countDocuments({ status: "done" });
    const admin = await User.countDocuments({ role: "admin" });
    const user = await User.countDocuments({ role: "user" });
    const data = {
      user: {
        title: `Users(${user + admin})`,
        data: [user, admin],
      },
      post: {
        title: `Post(${totalPost})`,
        data: [donePost, pendingPost, need_updatePost],
      },
    };
    return res.send(customResponse(data));
  } catch (error) {
    next(error);
  }
};

const statisticsTimeLine = async (req, res, next) => {
  try {
    const monthPost = await Post.aggregate([
      {
        $group: {
          _id: { $month: "$created_on" },
          count: { $sum: 1 },
        },
      },
      {
        $sort: { _id: 1 },
      },
      {
        $project: {
          count: 1,
          month: "$_id",
          _id: 0,
        },
      },
    ]);

    const monthUser = await User.aggregate([
      {
        $match: { role: "user" },
      },
      {
        $group: {
          _id: { $month: "$created_on" },
          count: { $sum: 1 },
        },
      },

      {
        $sort: { _id: 1 },
      },
      {
        $project: {
          count: 1,
          month: "$_id",
          _id: 0,
        },
      },
    ]);

    const resultUser = convertStaticsList(monthUser);
    const resultPost = convertStaticsList(monthPost);
    const data = { user: resultUser, post: resultPost };
    return res.send(customResponse(data));
  } catch (error) {
    console.log(error);
    next(error);
  }
};
const convertStaticsList = (inputList) => {
  const monthInput = inputList.map((e) => e.month);
  const monthNull = monthList.filter((n) => !monthInput.includes(n));
  const resultNull = [];
  monthNull.map((e) => {
    const item = {};
    item.month = e;
    item.count = 0;
    resultNull.push(item);
  });
  const resultList = resultNull
    .concat(inputList)
    .sort((a, b) => a.month - b.month);
  return resultList.map((e) => e.count);
};
export { statisticsNumber, statisticsTimeLine };
