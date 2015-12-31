	.file	"Good1.java"
	.section	.rodata
L1:	.string	"h"
	.align 8
L0:	.string	"hi"
	.align 8

	.align 8

	.section	.data
	.local v_1
	.comm v_1,8,8
	.local v_0
	.comm v_0,8,8

	.section	.text
	.align 8
	.globl main
	.type	main,@function
main:
	jmp	m_4
	.text
	.align 8
m_0:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$0, %rsp
L3:
f_0:
	jmp L2
L2:
	leave
	ret


	.text
	.align 8
m_1:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$0, %rsp
L5:
	movq $0,%rax
g_0:
	jmp L4
L4:
	leave
	ret


	.text
	.align 8
m_2:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$0, %rsp
L7:
	leaq L0(%rip),%rax
h_0:
	jmp L6
L6:
	leave
	ret


	.text
	.align 8
m_3:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$0, %rsp
L9:
f_1:
	jmp L8
L8:
	leave
	ret


	.text
	.align 8
m_4:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$0, %rsp
L11:
	leaq L1(%rip),%rdi
	callq m_2
	movq %rax, %rdi 
	callq b_printString_0
main_0:
	jmp L10
L10:
	leave
	ret


