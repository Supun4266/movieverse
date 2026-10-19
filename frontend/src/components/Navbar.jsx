import { useState } from 'react'
import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../AuthContext.jsx'

export default function Navbar() {
  const { isLoggedIn, username, signOut } = useAuth()
  const [text, setText] = useState('')
  const navigate = useNavigate()

  function handleSearch(event) {
    event.preventDefault()
    if (text.trim()) navigate(`/search?q=${encodeURIComponent(text.trim())}`)
  }

  return (
    <header className="navbar">
      <Link to="/" className="logo">
        Movie<span>Verse</span>
      </Link>

      <nav className="nav-links">
        <NavLink to="/" end>Home</NavLink>
        {isLoggedIn && <NavLink to="/my-list">My List</NavLink>}
      </nav>

      <form className="search" onSubmit={handleSearch} role="search">
        <input
          type="search"
          placeholder="Search movies"
          aria-label="Search movies"
          value={text}
          onChange={(e) => setText(e.target.value)}
        />
      </form>

      <div className="nav-user">
        {isLoggedIn ? (
          <>
            <span className="avatar" title={username}>{username.charAt(0).toUpperCase()}</span>
            <button className="btn btn-ghost btn-small" onClick={signOut}>Sign out</button>
          </>
        ) : (
          <Link to="/login" className="btn btn-primary btn-small">Sign in</Link>
        )}
      </div>
    </header>
  )
}
