import { useState } from "react";
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Button from '@mui/material/Button';
import Select, { SelectChangeEvent } from '@mui/material/Select';
import SplitButton from "./components/SplitButton";
import { CircularProgress } from "@mui/material";
import DisplayArray from "./components/DisplayArray";
import { AuthorControllerService, AuthorOverviewProjection, BookControllerService, BookOverviewProjection, CancelablePromise } from "./lib";

const NUMBER_OF_AUTHORS = 100
const NUMBER_OF_BOOKS = 100


async function createAuthors(backendBaseUrl: string): Promise<number[]> {
  const authorPromises: CancelablePromise<AuthorOverviewProjection>[] = []
  for (let i = 0; i < NUMBER_OF_AUTHORS; i++) {
    const createAuthorPromise = AuthorControllerService.createAuthor(
      {
        name: `Author${i}`,
        yearBorn: i
      }, `http://${backendBaseUrl}`
    )
    authorPromises.push(createAuthorPromise)
  }
  const promiseResults = await Promise.allSettled(authorPromises);
  const results: number[] = []
  for (const promiseResult of promiseResults) {
    if (promiseResult.status == "fulfilled") {
      results.push(promiseResult.value.id!)
    }
  }
  return results
}

async function createBooks(backendBaseUrl: string, authorIds: number[]): Promise<number[]> {
  const bookPromises: CancelablePromise<BookOverviewProjection>[] = []

  for (let i = 0; i < NUMBER_OF_BOOKS; i++) {
    const randomIndex = Math.floor(Math.random() * authorIds.length)
    const createBookPromise = BookControllerService.createBook(
      {
        authorId: authorIds[randomIndex],
        name: `Book${i}`,
        yearPublished: i
      }, `http://${backendBaseUrl}`
    )
    bookPromises.push(createBookPromise)
  }
  const promiseResults = await Promise.allSettled(bookPromises);
  const results: number[] = []
  for (const promiseResult of promiseResults) {
    if (promiseResult.status == "fulfilled") {
      results.push(promiseResult.value.id!)
    }
  }
  return results
}

function App() {
  const [backendBaseUrl, setBackendBaseUrl] = useState<"localhost:8083" | "localhost:8084">("localhost:8083");

  const [loading, setLoading] = useState<boolean>(false);

  const [averageRequestTime, setAverageRequestTime] = useState<number>(0);
  const [averageRequestLength, setAverageRequestLength] = useState<number>(0);

  const [responses, setResponses] = useState<string[]>([])
  const [authorIds, setAuthorIds] = useState<number[]>([])
  const [bookIds, setBookIds] = useState<number[]>([])


  const resetResponses = () => {
    setResponses([])
    setAverageRequestLength(0)
    setAverageRequestTime(0)
  }

  const handleBackendChange = (event: SelectChangeEvent) => {
    setBackendBaseUrl(event.target.value as typeof backendBaseUrl);
    resetResponses()
    setAuthorIds([])
    setBookIds([])
  };

  const generateNewData = () => {
    setLoading(true)
    resetResponses()
    const requestHandling = async () => {
      const authorIds = await createAuthors(backendBaseUrl)
      const bookIds = await createBooks(backendBaseUrl, authorIds)
      setAuthorIds(authorIds)
      setBookIds(bookIds)
      setResponses(["Authors and books are created."])
      setLoading(false)
    }
    requestHandling()
  }

  const handleFullDataFetch = () => {
    resetResponses()
    setLoading(true)

    const handleAsync = async () => {
      const validResponses: string[] = []
      let messageLen = 0
      let messageCount = 0
      let duration = 0;

      for (let i = 0; i < authorIds.length; i++) {
        try {
          const startTime = performance.now();
          const result = await AuthorControllerService.getAuthorById(authorIds[i], `http://${backendBaseUrl}`);
          const endTime = performance.now();
          duration += endTime - startTime
          const message = JSON.stringify(result)
          validResponses.push(message)
          messageLen += message.length
          messageCount++
        } catch (ignored) {
        }
      }

      for (let i = 0; i < bookIds.length; i++) {
        try {
          const result = await BookControllerService.getBookById(bookIds[i], `http://${backendBaseUrl}`);
          const message = JSON.stringify(result)
          validResponses.push(message)
          messageLen += message.length
          messageCount++
        } catch (ignored) {
        }
      }

      setResponses(validResponses)
      setAverageRequestLength(Math.floor(messageLen / validResponses.length))
      setLoading(false)
      setAverageRequestTime(Math.floor(duration / messageCount))
    }
    handleAsync();
  }

  const handleCacheableFullDataFetch = () => {
    resetResponses()
    setLoading(true)

    const handleAsync = async () => {
      const validResponses: string[] = []
      let messageLen = 0
      let messageCount = 0
      let duration = 0;

      for (let i = 0; i < authorIds.length; i++) {
        try {
          const startTime = performance.now();
          const result = await AuthorControllerService.getAuthorByIdCached(authorIds[i], `http://${backendBaseUrl}`);
          const endTime = performance.now();
          duration += endTime - startTime
          const message = JSON.stringify(result)
          validResponses.push(message)
          messageLen += message.length
          messageCount++
        } catch (ignored) {
        }
      }

      for (let i = 0; i < bookIds.length; i++) {
        try {
          const result = await BookControllerService.getBookByIdCached(bookIds[i], `http://${backendBaseUrl}`);
          const message = JSON.stringify(result)
          validResponses.push(message)
          messageLen += message.length
          messageCount++
        } catch (ignored) {
        }
      }

      setResponses(validResponses)
      setAverageRequestLength(Math.floor(messageLen / validResponses.length))
      setLoading(false)
      setAverageRequestTime(Math.floor(duration / messageCount))
    }
    handleAsync();
  }

  const handleProjectedDataFetch = () => {
    resetResponses()
    setLoading(true)

    const handleAsync = async () => {
      const validResponses: string[] = []
      let messageLen = 0
      let messageCount = 0
      let duration = 0;

      for (let i = 0; i < authorIds.length; i++) {
        try {
          const startTime = performance.now();
          const result = await AuthorControllerService.getAuthorOverviewById(authorIds[i], `http://${backendBaseUrl}`);
          const endTime = performance.now();
          duration += endTime - startTime
          const message = JSON.stringify(result)
          validResponses.push(message)
          messageLen += message.length
          messageCount++
        } catch (ignored) {
        }
      }

      for (let i = 0; i < bookIds.length; i++) {
        try {
          const result = await BookControllerService.getBookOverviewById(bookIds[i], `http://${backendBaseUrl}`);
          const message = JSON.stringify(result)
          validResponses.push(message)
          messageLen += message.length
          messageCount++
        } catch (ignored) {
        }
      }

      setResponses(validResponses)
      setAverageRequestLength(Math.floor(messageLen / validResponses.length))
      setLoading(false)
      setAverageRequestTime(Math.floor(duration / messageCount))
    }
    handleAsync();
  }

  return (
    <>
      <div className="p-8 flex flex-col items-center justify-center">
        <h1 className="text-xl text-white font-extrabold mb-8">Simple Book Management CRUD App Benchmarking</h1>
        <div className="flex flex-row flex-grow border-2 max-w-prose p-4 rounded w-full items-center justify-center space-x-2">
          <div>
            <FormControl fullWidth>
              <InputLabel id="backend-select-label">Backend</InputLabel>
              <Select
                labelId="backend-select-label"
                id="backend-select"
                value={backendBaseUrl}
                defaultValue="java-backend"
                label="Backend"
                onChange={handleBackendChange}
                disabled={loading}
              >
                <MenuItem value={"localhost:8083"}>Java</MenuItem>
                <MenuItem value={"localhost:8084"}>Go</MenuItem>
              </Select>
            </FormControl>
          </div>
          <div>
            <Button variant="contained" disabled={loading} onClick={() => generateNewData()}>Generate Random Data</Button>
          </div>
          <div>
            <SplitButton disabled={loading || authorIds.length == 0} options={["Fetch Full Data (No Cache)", "Fetch Full Data (Cache)", "Fetch Projected Data"]} handleClickFunctions={[
              handleFullDataFetch, handleCacheableFullDataFetch, handleProjectedDataFetch
            ]} />
          </div>
        </div>
        <div className="border-2 max-w-prose p-4 w-full grid grid-cols-2 rounded justify-items-center mt-2">
          <h2 className="text-emerald-200">Avg. Time:{` ${averageRequestTime} ms`}</h2>
          <h2 className="text-red-200">Avg. Body Length:{` ${averageRequestLength} characters`}</h2>
        </div>
        {
          loading && <CircularProgress className="mt-8" />
        }
        {!loading && (responses.length > 0) &&
          <div className="max-w-prose w-full mt-2 p-4 rounded border-2">
            <h2 className="mb-2 text-emerald-400">Responses (truncated)</h2>
            <DisplayArray strings={responses} />
          </div>
        }
      </div>
    </>
  )
}

export default App
