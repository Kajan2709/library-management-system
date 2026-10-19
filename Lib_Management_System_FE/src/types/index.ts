export type Gender = "M" | "F";

export interface Student {
  studentId: number;
  studentName: string;
  age: number;
  gender: Gender;
  course: string;
  fineAmount: number;
}

export interface Book {
  bookId: number;
  bookTitle: string;
  author: string;
  category: string;
  availableQuantity: number;
}

export interface BorrowRecord {
  borrowRecordId: number;
  studentId: number;
  studentName?: string;
  bookId: number;
  bookTitle?: string;
  borrowDate: string;
  returnDate: string | null;
  numberOfBooks: number;
  paymentStatus?: "PAID" | "UNPAID" | null;
  finePerBook?: number;
  totalFine?: number;
}

export interface StudentInput {
  studentName: string;
  age: number;
  gender: Gender;
  course: string;
}

export interface BookInput {
  bookTitle: string;
  author: string;
  category: string;
  availableQuantity: number;
}

export interface BorrowInput {
  studentId: number;
  bookId: number;
  numberOfBooks: number;
  borrowDate?: string;
  returnDate?: string | null;
  paymentStatus?: "PAID" | "UNPAID";
}

export interface FineInput {
  studentId: number;
  lateDays: number;
}

export interface LibrarySummary {
  totalStudents: number;
  totalMaleStudents: number;
  totalFemaleStudents: number;
  totalCurrentlyBorrowedBooks: number;
  studentsWith0Books: number;
  studentsWith1Books: number;
  studentsWith2Books: number;
  studentsWith3Books: number;
  studentsWith4Books: number;
  studentsWith5Books: number;
  totalRegisteredBooks: number;
  totalAvailableBooks: number;
  totalBorrowedBooks: number;
}

export type PaymentStatus = "PAID" | "UNPAID";

export interface ReturnInput {
  studentId: number;
  bookId: number;
  numberOfBooks: number;
  returnDate: string;
  paymentStatus: PaymentStatus;
}
