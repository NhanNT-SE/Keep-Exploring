import express from "express";
import swaggerJsdoc from "swagger-jsdoc";
import swaggerUi from "swagger-ui-express";
import swaggerDefinition from "./swaggerDef.js";
const router = express.Router();

const swaggerDoc = swaggerJsdoc({
  swaggerDefinition,
  apis: ["src/docs/*.yml", "src/Route/*.js"],
});

router.use("/", swaggerUi.serve);
router.get(
  "/",
  swaggerUi.setup(swaggerDoc, {
    explorer: true,
  })
);

export default router;
