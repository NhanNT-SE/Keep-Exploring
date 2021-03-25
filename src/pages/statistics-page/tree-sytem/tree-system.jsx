import React, { useState } from "react";
import { OrganizationChart } from "primereact/organizationchart";

import "./tree-system.scss";
function TreeSystem() {
  const [selection, setSelection] = useState([]);
  const data = [
    {
      label: "KEEP EXPLORING",
      expanded: true,
      className: "department-cfo",
      children: [
        {
          label: "NGUYEN TRONG NHAN",
          type: "person",
          className: "p-person",
          expanded: true,
          data: { name: "Team Lead", avatar: "avatar1.png" },
          children: [
            {
              label: "Devops-AWS",
              className: "department-cfo",
            },
            {
              label: "Frontend-ReactJs",
              className: "department-cfo",
            },
          ],
        },
        {
          label: "NGUYEN THI KIM NGAN",
          type: "person",
          className: "p-person",
          expanded: true,
          data: { name: "Member", avatar: "kim-ngan.jpg" },
          children: [
            {
              label: "Database-MongoDB",
              className: "department-cfo",
            },
            {
              label: "Backend-NodeJs",
              className: "department-cfo",
            },
          ],
        },
        {
          label: "TRAN QUOC HAO",
          type: "person",
          className: "p-person",
          expanded: true,
          data: { name: "Member", avatar: "hao.jpg" },
          children: [
            {
              label: "Mobile-Android",
              className: "department-cfo",
            },
          ],
        },
        {
          label: "BUI DUY LAM",
          type: "person",
          className: "p-person",
          expanded: true,
          data: { name: "Member", avatar: "lam.jpg" },
          children: [
            {
              label: "Mobile-Android",
              className: "department-cfo",
            },
          ],
        },
        {
          label: "CAO HOANG PHUOC",
          type: "person",
          className: "p-person",
          expanded: true,
          data: { name: "Member", avatar: "phuoc.jpg" },
          children: [
            {
              label: "Mobile-Android",
              className: "department-cfo",
            },
          ],
        },
      ],
    },
  ];
  const nodeTemplate = (node) => {
    if (node.type === "person") {
      return (
        <div>
          <div className="node-header">{node.label}</div>
          <div className="node-content">
            <img
              alt={node.data.avatar}
              src={`${process.env.PUBLIC_URL}/images/${node.data.avatar}`}
              onError={(e) =>
                (e.target.src =
                  "https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png")
              }
              style={{ width: "32px" }}
            />
            <div>{node.data.name}</div>
          </div>
        </div>
      );
    }

    if (node.type === "department") {
      return node.label;
    }
  };
  return (
    <OrganizationChart
      value={data}
      nodeTemplate={nodeTemplate}
      selection={selection}
      selectionMode="multiple"
      onSelectionChange={(event) => setSelection(event.data)}
      className="company"
    ></OrganizationChart>
  );
}

export default TreeSystem;
