import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux'

const Search = () => {
  const [movies, setMovies] = useState([]);
  const Api_key = "c45a857c193f6302f2b5061c3b85e743";
  const movie_name = useSelector((state) => state.search.value);
  console.log(movie_name);

  //https://api.themoviedb.org/3/search/movie?api_key=${Api_key}&language=en-US&query=${movie_name}&page=1

  const fetchSearchMovie = async (movie_name) => {
    try {
      let res = await axios.get(
        `https://api.themoviedb.org/3/search/movie?api_key=${Api_key}&language=en-US&query=${movie_name}&page=1`
      );
      console.log(res.data.results);
      setMovies(res.data.results);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchSearchMovie(movie_name);
  }, [movie_name]);
  

  return (
    <>
      <div className="container mb-5 mt-5">
        <div className="row mb-3 mt-3">
          {/* <h2 className="text-center">Popular Movies</h2> */}
          {movies &&
            movies.length > 0 &&
            movies.map((movie) => {
              return (
                <div className="col-xl-3 text-center p-3 single">
                  <img
                    src={`https://image.tmdb.org/t/p/w500${movie.poster_path}`}
                    alt=""
                    className="img-fluid"
                  />
                  <h4 className="mt-3">{movie.title}</h4>
                  <p>
                    Rating: <strong>{movie.vote_average}</strong>
                  </p>
                </div>
              );
            })}
        </div>
      </div>
    </>
  );
}

export default Search