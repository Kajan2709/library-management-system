/** Base URL and API endpoint paths used by the application. */
export const API_BASE_URL = ""; 
//export const API_BASE_URL_LOCAL = "http://localhost:8080";


export const API_ENDPOINTS = {
  students: "/api/students",
  studentsAll: "/api/getstudents",
  studentById: (id: number) => `/api/students/${id}`,
  studentsByGender: (gender: string) => `/api/students/gender/${gender}`,
  studentFine: (id: number) => `/api/students/${id}/fine`,
  studentFinePayment: (id: number) => `/api/students/${id}/fine/payment`,
  books: "/api/books",
  booksAll: "/api/getbooks",
  borrow: "/api/borrow",
  return: "/api/return",
  librarySummary: "/api/summary",
} as const;
