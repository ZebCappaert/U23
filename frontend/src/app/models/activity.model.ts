import { Attendance } from "./attendance.model";

export interface Activity {
    id?: number;
    title: string;
    dateTime: string;
    location: string;
    attendances?: Attendance[];
}