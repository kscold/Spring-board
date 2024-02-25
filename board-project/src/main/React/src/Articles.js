import { useEffect, useState } from "react"
import axios from "axios"

function TestAPI() {
  const [hello, setHello] = useState("")

  useEffect(() => {
    axios.get("/api/articles").then((res) => {
      setHello(res.data)
    })
  }, [])
  return <div>백엔드 데이터 : {hello}</div>
}

export default TestAPI
