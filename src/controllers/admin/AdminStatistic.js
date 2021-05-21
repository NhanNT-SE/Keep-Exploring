import fs from "fs";
import Post from "../../models/Post.js";
import Blog from "../../models/Blog.js";
import User from "../../models/User.js";
import {customError} from "../../helpers/CustomError.js";
const monthList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

const statisticsNumber = async (req, res, next) => {
  try {
    const totalPost = await Post.countDocuments({});
    const totalBlog = await Blog.countDocuments({});
    const pendingPost = await Post.countDocuments({ status: "pending" });
    const pendingBlog = await Blog.countDocuments({ status: "pending" });
    const need_updatePost = await Post.countDocuments({
      status: "need_update",
    });
    const need_updateBlog = await Blog.countDocuments({
      status: "need_update",
    });
    const donePost = await Post.countDocuments({ status: "done" });
    const doneBlog = await Blog.countDocuments({ status: "done" });
    const admin = await User.countDocuments({ role: "admin" });
    const user = await User.countDocuments({ role: "user" });
    const data = {
      user: {
        title: `Users(${user + admin})`,
        data: [user, admin],
      },
      postBlog: {
        title: `Post-Blog(${totalPost + totalBlog})`,
        data: [totalPost, totalBlog],
      },
      post: {
        title: `Post(${totalPost})`,
        data: [donePost, pendingPost, need_updatePost],
      },
      blog: {
        title: `Blog(${totalBlog})`,
        data: [doneBlog, pendingBlog, need_updateBlog],
      },
    };
    return res
      .status(200)
      .send({ data, status: 200, message: "Lấy dữ liệu thành công" });
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
    const monthBlog = await Blog.aggregate([
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
    const resultBlog = convertStaticsList(monthBlog);
    return res.status(200).send({
      data: { user: resultUser, post: resultPost, blog: resultBlog },
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
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