import { Link } from 'react-router-dom'
import Message from '../components/Message.jsx'

export default function NotFound() {
  return (
    <div className="page">
      <Message>This page does not exist. <Link to="/">Go home</Link></Message>
    </div>
  )
}
