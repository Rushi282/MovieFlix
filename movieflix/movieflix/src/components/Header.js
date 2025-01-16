import React, { useRef } from "react";
import logo from '../assets/MovieFlixLogo.png'
import { Link, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import {getSearchQuery} from '../store/slices/searchSlice'

export default function Header() {

  const title = useRef();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault();
    console.log(title.current.value);
    dispatch(getSearchQuery(title.current.value));
    navigate('/search');

    document.querySelector(".iBox").value = "";
  }

  return (
    <>
      <div className="bg-danger bg-gradient">
        <nav class="navbar navbar-expand-lg container">
          <div class="container-fluid">
            <Link class="navbar-brand" to="/">
              <img src={logo} alt="" width={120} />
            </Link>
            <button
              class="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarSupportedContent"
              aria-controls="navbarSupportedContent"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
              <ul class="navbar-nav me-auto mb-2 mb-lg-0 menus">
                <li class="nav-item">
                  <Link class="nav-link active" aria-current="page" to="/">
                    Popular
                  </Link>
                </li>
                <li class="nav-item">
                  <Link
                    class="nav-link active"
                    aria-current="page"
                    to="/upcoming"
                  >
                    Upcoming
                  </Link>
                </li>
                <li class="nav-item">
                  <Link class="nav-link active" aria-current="page" to="/top">
                    Top Rated
                  </Link>
                </li>
                <li class="nav-item">
                  <Link class="nav-link active" aria-current="page" to="/local">
                    Local Movies
                  </Link>
                </li>
              </ul>
              <form class="d-flex" role="search" onSubmit={handleSearch}>
                <input
                  class="form-control me-2 iBox"
                  type="search"
                  placeholder="Search"
                  aria-label="Search"
                  ref={title}
                />
                <button class="btn btn-outline-dark" type="submit">
                  Search
                </button>
              </form>
            </div>
          </div>
        </nav>
      </div>
    </>
  );
}