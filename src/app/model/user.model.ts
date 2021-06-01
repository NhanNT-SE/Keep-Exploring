export interface User {
  id: string;
  username: string;
  email: string;
  displayName?: string;
  avatar?: string;
  role: 'admin' | 'user';
}
