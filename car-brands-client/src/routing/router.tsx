import { createBrowserRouter } from "react-router-dom";
import { Edit } from "../views/Edit";
import { Details } from "../views/Details";
import { Create } from "../views/Create";
import { Home } from "../views/Home";

export const router = createBrowserRouter([
  { path: "/", element: <Home/> },
  { path: "/edit/:id", element: <Edit/> },
  { path: "/details/:id", element: <Details/> },
  { path: "/create", element: <Create/> },
]);
