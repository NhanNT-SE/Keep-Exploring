import { User } from '../../model/user.model';
export interface AuthState {
  user: User | null;
  status: 'loading' | 'success' | 'fail' | 'idle';
  error?: string;
}
