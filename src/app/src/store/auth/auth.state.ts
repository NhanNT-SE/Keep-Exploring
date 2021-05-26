import { User } from './../../model/user.model';
export interface AuthState {
  userList: User[];
  user: User | null;
  status: 'loading' | 'success' | 'fail' | 'idle';
  error?: string;
  sort: 'asc' | 'desc' | null;
}
