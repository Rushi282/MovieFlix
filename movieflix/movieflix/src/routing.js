import React from "react";
import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import Popular from "./components/Popular";
import Upcoming from "./components/Upcoming";
import TopRated from "./components/TopRated";
import Search from "./components/Search";
import MovieDetails from "./components/MovieDetails";
import { LocalMovies } from "./components/LocalMovies";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      {
        path: "/",
        element: <Popular />,
      },
      {
        path: "/upcoming",
        element: <Upcoming />,
      },
      {
        path: "/top",
        element: <TopRated />,
      },
      {
        path: "/search",
        element: <Search />,
      },
      {
        path: "/movie",
        element: <MovieDetails />,
      },
      {
        path: "/local",
        element: <LocalMovies/>,
      },
    ],
  },
]);

export default router;
