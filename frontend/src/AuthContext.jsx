import { createContext, useCallback, useContext, useMemo, useState } from 'react'

// Shares "who is logged in" with every page.
const AuthContext = createContext(null)
const STORAGE_KEY = 'movieverse-auth'

function loadSaved() {
  try {
    const saved = JSON.parse(localStorage.getItem(STORAGE_KEY))
    if (saved && saved.token && saved.username) return saved
  } catch {
    // storage blocked or empty
  }
  return null
}

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(loadSaved)

  const signIn = useCallback((data) => {
    const value = { token: data.token, username: data.username }
    setAuth(value)
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(value))
    } catch {
      // ignore
    }
  }, [])

  const signOut = useCallback(() => {
    setAuth(null)
    try {
      localStorage.removeItem(STORAGE_KEY)
    } catch {
      // ignore
    }
  }, [])

  const value = useMemo(
    () => ({ token: auth?.token, username: auth?.username, isLoggedIn: !!auth, signIn, signOut }),
    [auth, signIn, signOut],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  return useContext(AuthContext)
}
