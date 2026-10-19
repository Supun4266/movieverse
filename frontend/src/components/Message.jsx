export default function Message({ children, tone = 'info' }) {
  return <div className={`message message-${tone}`}>{children}</div>
}
