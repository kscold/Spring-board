import { useEffect, useState } from "react"
import axios from "axios"

function App() {
  const [articles, setArticles] = useState([])

  useEffect(() => {
    const fetchArticles = async () => {
      try {
        const response = await axios.get("/api/articles")
        setArticles(response.data._embedded.articles)
      } catch (error) {
        console.error("Error fetching articles:", error)
      }
    }

    fetchArticles()
  }, [])

  return (
    <div className="App">
      <h1>백엔드 데이터:</h1>
      <ul>
        {articles.map((article, index) => (
          <li key={index}>
            <h2>{article.title}</h2>
            <p>
              <strong>Content:</strong> {article.content}
            </p>
            <p>
              <strong>Created At:</strong> {article.createdAt}
            </p>
            <p>
              <strong>Created By:</strong> {article.createdBy}
            </p>
            <p>
              <strong>Hashtag:</strong> {article.hashtag}
            </p>
            <p>
              <strong>Modified At:</strong> {article.modifiedAt}
            </p>
            <p>
              <strong>Modified By:</strong> {article.modifiedBy}
            </p>
          </li>
        ))}
      </ul>
    </div>
  )
}

export default App
